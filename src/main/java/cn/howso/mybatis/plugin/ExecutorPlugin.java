package cn.howso.mybatis.plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
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

import cn.howso.mybatis.util.ReflectHelper;
import cn.howso.mybatis.util.ScriptSqlProvider;
import cn.howso.mybatis.util.XMLMapperConf;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class }) })
public class ExecutorPlugin implements Interceptor {
	private String dialect;
	private String optiColumnName;
	
    //private static final Logger logger = Logger.getLogger(ExecutorPlugin.class);
    private static final Map<String, Method> providerMethodMap = new HashMap<>();
    static {
        Method[] methods = ScriptSqlProvider.class.getMethods();
        for (Method m : methods) {
            providerMethodMap.put(m.getName(), m);
        }
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Configuration configuration = statement.getConfiguration();
        Object parameterObject = args[1];
        //statement.getSqlSource().getBoundSql(parameterObject);
        String sql = statement.getBoundSql(parameterObject).getSql();
        String statementId = statement.getId();
        int index = statementId.lastIndexOf(".");
        String methodName = statementId.substring(index + 1);
        if (("howso-" + methodName).equals(sql)) {// 只有第一次会执行，因为执行一次后sql语句已经被改变。
            synchronized(this){
                if (("howso-" + methodName).equals(sql)) {
                    String mapperClazzName = statementId.substring(0, index);
                    ScriptSqlProvider provider = new ScriptSqlProvider();
                    Method method = providerMethodMap.get(methodName);
                    String script = (String) method.invoke(provider, XMLMapperConf.of(configuration, mapperClazzName,dialect,optiColumnName));
                    // 不支持写<selectKey>，不支持<include>
                    // "<script>select * from sys_user <where> 1=1</where>order by #{orderByClause}</script>";
                    Class<?> paramClazz = parameterObject==null?null:parameterObject.getClass();
                    SqlSource dynamicSqlSource = new XMLLanguageDriver().createSqlSource(configuration, script,
                            paramClazz);
                    //String msg = "sql for %s is %s,replaced by\r\n%s";
                    //logger.debug(String.format(msg, statementId,sql,script));
                    ReflectHelper.setValueByFieldName(statement, "sqlSource", dynamicSqlSource);
                }
            }
        }
        Object ret = invocation.proceed();
        return ret;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    	dialect = properties.getProperty("dialect");
    	if(dialect==null){
    		dialect = "postgresql";
    	}
    	Set<String> dialects = new HashSet<>();
    	dialects.add("postgresql");
    	dialects.add("mysql");
    	if(!dialects.contains(dialect)){
    		throw new RuntimeException("dialect="+dialect+" 不被支持,目前仅支持"+String.join(",", dialects));
    	}
    	optiColumnName = properties.getProperty("optiColumnName");
    	if(optiColumnName==null){
    		optiColumnName = "opti_version";
    	}
    }
}
