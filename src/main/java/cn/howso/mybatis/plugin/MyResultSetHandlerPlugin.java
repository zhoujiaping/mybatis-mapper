package cn.howso.mybatis.plugin;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import cn.howso.mybatis.model.User;
/**
 * 目标：只管在xml中写sql语句，自动对结果集做处理，包括驼峰命名处理，一对多，一对一关系处理。
 * 嵌套对象处理，要使用约定的命名规则，通过使用 字段名$字段名 的规则，自动处理为 属性名.属性名。
 * */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class MyResultSetHandlerPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(1==1)return invocation.proceed();
		System.out.println(invocation);
		Object[] args = invocation.getArgs();
		// 获取到当前的Statement
		Statement stmt = (Statement) args[0];
		// 通过Statement获得当前结果集
		ResultSet resultSet = stmt.getResultSet();
		List<Object> resultList = new ArrayList<Object>();
			// infos字段
			while(resultSet.next()){
				User u = new User();
				ResultSetMetaData rsm = resultSet.getMetaData();
				int columnCount = rsm.getColumnCount();
				for(int i=1;i<=columnCount;i++){
					String label = rsm.getColumnLabel(i);
					if(label.equals("nick"))
					u.setNick((String)resultSet.getObject(label));
				}
				resultList.add(u);
			}
			return resultList;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

}
