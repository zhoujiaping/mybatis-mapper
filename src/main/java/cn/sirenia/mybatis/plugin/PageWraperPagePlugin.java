package cn.sirenia.mybatis.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import cn.sirenia.mybatis.plugin.model.PageResWraper;
import cn.sirenia.mybatis.plugin.model.Pageable;
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
 * 
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PageWraperPagePlugin implements Interceptor {

	private String dialect = ""; // 数据库方言

	private ThreadLocal<Pageable> pageHolder = new ThreadLocal<>();
	private Set<String> pageableStatementIds = new HashSet<>();

	public Object intercept(Invocation ivk) throws Throwable {
		Object target = ReflectHelper.unwrapProxys(ivk.getTarget());
		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) target;
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");
			if (pageable(mappedStatement)) { // 拦截需要分页的SQL
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject尚未实例化！");
				} else {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String countSql = "select count(0) from (" + sql + ") as tmp_count"; // 记录统计
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					statementHandler.getParameterHandler().setParameters(countStmt);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					Pageable page = findPageObject(parameterObject);
					page.setTotal(count);
					String pageSql = page.createPageSql(sql, dialect);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
					pageHolder.set(page);
				}
			}
		} else if (target instanceof ResultSetHandler) {
			ResultSetHandler resultSetHandler = (ResultSetHandler) target;
			Object mappedStatementObject = ReflectHelper.getValueByFieldName(resultSetHandler, "mappedStatement");
			MappedStatement mappedStatement = (MappedStatement) mappedStatementObject;
			if (pageable(mappedStatement)) {
				try {
					List<?> list = (List<?>) ivk.proceed();
					Pageable page = pageHolder.get();
					return PageResWraper.of(list, page);
				} finally {
					pageHolder.remove();
				}
			}
		}
		return ivk.proceed();
	}

	private boolean pageable(MappedStatement mappedStatement) throws ClassNotFoundException {
		String statementId = mappedStatement.getId();
		if (pageableStatementIds.contains(statementId)) {
			return true;
		}
		synchronized (this) {
			if (pageableStatementIds.contains(statementId)) {
				return true;
			}
			int index = statementId.lastIndexOf(".");
			String methodName = statementId.substring(index + 1);
			String mapperClazzName = statementId.substring(0, index);
			Class<?> mapperClazz = Class.forName(mapperClazzName);
			Method[] methods = mapperClazz.getMethods();
			Method method = null;
			for (Method m : methods) {
				if (m.getName().equals(methodName)) {
					method = m;
					break;
				}
			}
			Class<?> retType = method.getReturnType();
			if (PageResWraper.class.isAssignableFrom(retType)) {
				pageableStatementIds.add(statementId);
				return true;
			}
			/*
			 * if(retType.getName().equals(PageRes2.class.getName())){
			 * pageableStatementIds.add(statementId); return b; }
			 */
		}
		return false;
	}

	private Pageable findPageObject(Object parameterObject)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		if (parameterObject instanceof Pageable) {
			return (Pageable) parameterObject;
		} else if (parameterObject instanceof Map) {
			Map<?, ?> temp = (Map<?, ?>) parameterObject;
			Object obj = temp.get("page");
			if (obj instanceof Pageable) {
				return (Pageable) obj;
			} else {
				throw new RuntimeException("不存在 page 属性！");
			}
		} else {
			Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
			if (pageField != null) {
				Object page = ReflectHelper.getValueByFieldName(parameterObject, "page");
				if (page == null) {
					throw new RuntimeException("属性page值为null");
				}
				return (Pageable) page;
			} else {
				throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 page 属性！");
			}
		}

	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (dialect == null || dialect.trim().isEmpty()) {
			new PropertyException("dialect property is not found!").printStackTrace();
		}
		dialect = dialect.toLowerCase();
	}
}
