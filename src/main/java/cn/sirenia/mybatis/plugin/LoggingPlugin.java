package cn.sirenia.mybatis.plugin;

import java.util.Collection;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * mybatis打印sql的日志是debug级别才打印。
 * 但是如果我们把日志级别改为debug，日志中会包含无数的其他debug日志。
 * 这是一种不好的设计。个人认为更好的方式是通过配置的方式，给个单独的开关，而不是根据日志级别判定。
 * 因为日志级别影响的范围太广了。
 * 所以开发此插件，通过配置是否启用该插件，决定是否打印sql日志。
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class LoggingPlugin implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggingPlugin.class);

	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement statement = (MappedStatement) args[0];
		//Configuration configuration = statement.getConfiguration();
		Object parameterObject = args[1];
		String parameterJson = toJSONString(parameterObject);
		//statement.getSqlSource().getBoundSql(parameterObject);
		String sql = statement.getBoundSql(parameterObject).getSql();
		logger.info(" - ==>  Preparing: {}",sql);
		logger.info(" - ==>  Parameters: {}",parameterJson);
		//String statementId = statement.getId();
		Object ret = invocation.proceed();
		String retJson = toJSONString(ret);
		logger.info(" - ==>  Result(s): {}",retJson);
		return ret;
	}

	private String toJSONString(Object obj){
		String jsonString = null;
		if(obj!=null){
			Class<?> parameterClazz = obj.getClass();
			if(parameterClazz.isArray()){
				jsonString = JSONArray.toJSONString(obj);
			}else if(Collection.class.isAssignableFrom(parameterClazz)){
				jsonString = JSONArray.toJSONString(obj);
			}else{
				jsonString = JSONObject.toJSONString(obj);
			}
		}else{
			jsonString = JSONObject.toJSONString(obj);
		}
		return jsonString;
	}
	
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	public void setProperties(Properties p) {
	}
}
