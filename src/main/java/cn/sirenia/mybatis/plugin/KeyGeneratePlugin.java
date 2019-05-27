package cn.sirenia.mybatis.plugin;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import cn.sirenia.mybatis.anno.KeyGen;
import cn.sirenia.mybatis.enums.KeyGeneratorType;
import cn.sirenia.mybatis.keygen.MyJdbc3KeyGenerator;
import cn.sirenia.mybatis.util.BeanUtil;
import cn.sirenia.mybatis.util.ReflectHelper;

/**
 * 
 * mybatis插件实际上就是拦截器，需要实现Interceptor接口。
 * 可以拦截StatementHandler、Executor、ResultSetHandler、ParameterHandler
 * 
 * 通过@Intercepts和@Signature指定被拦截的接口及其方法
 * 
 * StatementHandler --BaseStatementHandler（abstract）
 * ----CallableStatementHandler ----PreparedStatementHandler
 * ----SimpleStatementHandler --RoutingStatementHandler
 *  * configuration.xml中可以配置settings的useGeneratedKeys为true，
 * 但是需要mapper.xml中的insert标签有keyProperty属性才能有效。
 * 所以该插件的另外一个思路，是配置全局的useGeneratedKeys为true，
 * 然后获取@KeyGen注解，获取keyProperties和keyColumn，
 * 设置到MappedStatement对象中。
 * 
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class KeyGeneratePlugin implements Interceptor {

	//private String dialect = ""; // 数据库方言
	private KeyGeneratorType keyGeneratorType;

	private Map<String, String> statementIdsDealedKeyGen = new ConcurrentHashMap<>();

	public Object intercept(Invocation ivk) throws Throwable {
		if (keyGeneratorType == null) {
			return ivk.proceed();
		}
		Object target = ReflectHelper.unwrapProxys(ivk.getTarget());
		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) target;
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");
			SqlCommandType commandType = mappedStatement.getSqlCommandType();
			if (commandType == SqlCommandType.INSERT) {
				String stmtId = mappedStatement.getId();
				if (!statementIdsDealedKeyGen.containsKey(stmtId)) {
					dealKeyGen(delegate, mappedStatement, stmtId);
				}
			}
		}
		return ivk.proceed();
	}

	private synchronized void dealKeyGen(StatementHandler statementHandler, MappedStatement mappedStatement,
			String stmtId)
			throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
		if (statementIdsDealedKeyGen.containsKey(stmtId)) {
			return;
		}
		BoundSql boundSql = statementHandler.getBoundSql();
		Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
		if (parameterObject == null) {
			throw new NullPointerException("parameterObject尚未实例化！");
		}
		if (parameterObject instanceof Collection<?>) {
			statementIdsDealedKeyGen.put(stmtId, "");
			return;
		}
		String mapperName = stmtId.substring(0, stmtId.lastIndexOf("."));
		Class<?> mapperClass = Class.forName(mapperName);
		KeyGen keygenAnno = mapperClass.getAnnotation(KeyGen.class);
		if (keygenAnno == null) {
			statementIdsDealedKeyGen.put(stmtId, "");
			return;
		}
		String keyProperty = keygenAnno.keyProperty();
		String keyColumn = keygenAnno.keyColumn();
		if ((keyProperty == null || keyProperty.equals("")) && (keyColumn == null || keyColumn.equals(""))) {
			throw new RuntimeException("keyGen配置错误，keyPropertie和keyColumn不能都为空");
		} else if (keyProperty == null || keyProperty.equals("")) {
			keyProperty = BeanUtil.underline2camel(keyColumn);
		} else {
			keyColumn = BeanUtil.camel2underline(keyProperty);
		}
		// Object primaryKey =
		// ReflectHelper.getValueByFieldName(parameterObject, keyProperty);
		KeyGenerator kg = mappedStatement.getKeyGenerator();
		if (kg instanceof NoKeyGenerator) {
			switch (keyGeneratorType) {
			case UseGenerateKey:
				//kg = new Jdbc3KeyGenerator();
				//兼容加解密插件
				kg = new MyJdbc3KeyGenerator();
				ReflectHelper.setValueByFieldName(mappedStatement, "keyGenerator", kg);
				ReflectHelper.setValueByFieldName(mappedStatement, "keyProperties", new String[] { keyProperty });
				ReflectHelper.setValueByFieldName(mappedStatement, "keyColumns", new String[] { keyColumn });
				break;
			default:
				throw new RuntimeException("目前只支持UseGenerateKey");
			}
		}
		statementIdsDealedKeyGen.put(stmtId, "");
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties p) {
		/*
		 * dialect = p.getProperty("dialect"); if (dialect == null ||
		 * dialect.trim().isEmpty()) { throw new RuntimeException(
		 * "dialect property is not found!"); } dialect = dialect.toLowerCase();
		 */
		String keyGeneratorTypeName = p.getProperty("keyGeneratorType");
		if (keyGeneratorTypeName != null) {
			keyGeneratorType = KeyGeneratorType.valueOf(keyGeneratorTypeName);
			if (keyGeneratorType == null) {
				throw new RuntimeException("keyGeneratorType property is not correct!");
			}
		}
	}
}
