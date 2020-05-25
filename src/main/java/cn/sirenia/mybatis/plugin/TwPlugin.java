package cn.sirenia.mybatis.plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import cn.sirenia.mybatis.sql.provider.TwSqlProvider;
import cn.sirenia.mybatis.util.ReflectHelper;
import cn.sirenia.mybatis.util.XMLMapperConf;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
//AutoSqlExecutorPlugin的另一种实现方式，这个类改的是MappedStatement
public class TwPlugin implements Interceptor {

	// private static final Logger logger =
	// Logger.getLogger(ExecutorPlugin.class);
	private String dialect = "postgresql"; // 数据库方言
	private final Map<String, Method> providerMethodMap = new HashMap<>();
	{
		Method[] methods = TwSqlProvider.class.getMethods();
		for (Method m : methods) {
			providerMethodMap.put(m.getName(), m);
		}
	}

	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement statement = (MappedStatement) args[0];
		Configuration configuration = statement.getConfiguration();
		Object parameterObject = args[1];
		// statement.getSqlSource().getBoundSql(parameterObject);
		String sql = statement.getBoundSql(parameterObject).getSql();
		String statementId = statement.getId();
		int index = statementId.lastIndexOf(".");
		String methodName = statementId.substring(index + 1);
		if (("sirenia-" + methodName).equals(sql)) {// 只有第一次会执行，因为执行一次后sql语句已经被改变。
			synchronized (this) {
				if (("sirenia-" + methodName).equals(sql)) {
					//动态创建sql
					String mapperClazzName = statementId.substring(0, index);
					TwSqlProvider provider = new TwSqlProvider();
					Method method = providerMethodMap.get(methodName);
					Class<?> paramClazz = parameterObject == null ? null : parameterObject.getClass();
					String script = null;
					try{
						TwSqlProvider.tl.set(XMLMapperConf.of(configuration, mapperClazzName,dialect));
						script = (String) method.invoke(provider);
					}finally{
						TwSqlProvider.tl.remove();
					}
					//构建MappedStatement
					SqlSource sqlSource = new XMLLanguageDriver().createSqlSource(configuration, script.toString(),
							paramClazz);
					SqlCommandType sqlCommandType = statement.getSqlCommandType();
					MappedStatement ms = new MappedStatement.Builder(configuration, statementId, sqlSource, sqlCommandType )
							.resultMaps(statement.getResultMaps() ).build();
					Map<String, MappedStatement> mappedStatements = (Map<String, MappedStatement>) ReflectHelper.getValueByFieldName(configuration, "mappedStatements");
					//以后执行使用新的MappedStatement
					mappedStatements.remove(statementId);
					mappedStatements.put(statementId, ms);
					//本次执行也要使用新的MappedStatement
					args[0] = ms;
				}
			}
		}
		Object ret = invocation.proceed();
		return ret;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	public void setProperties(Properties p) {
		String dialect = p.getProperty("dialect");
		if (dialect != null && !dialect.trim().isEmpty()) {
			this.dialect = dialect;
		}
	}
}
