package cn.howso.mybatis.plugin;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.PropertyException;

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

import cn.howso.mybatis.model.Pageable;
import cn.howso.mybatis.util.ReflectHelper;

/**
 * <p>
 * mybatis的分页插件
 * </p>
 * 
 * @author wzf
 * @date 2016年3月15日 上午9:44:22
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}) })
public class PagePlugin implements Interceptor {

    private static String dialect = ""; // 数据库方言

    private static String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)

    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
                    "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
                    "mappedStatement");
            if (mappedStatement.getId().matches(pageSqlId)) { // 拦截需要分页的SQL
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
                }
            }
        }
        return ivk.proceed();
    }

    private Pageable findPageObject(Object parameterObject)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (parameterObject instanceof Pageable) {
            return (Pageable) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map<?,?> temp = (Map<?,?>) parameterObject;
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

    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (dialect==null || dialect.trim().isEmpty()) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (pageSqlId==null || pageSqlId.trim().isEmpty()) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
    }
}
