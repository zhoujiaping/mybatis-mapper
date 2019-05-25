package cn.sirenia.mybatis.keygen;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import cn.sirenia.mybatis.util.ParamObjectHolder;
/**
 * 用来替换Jdbc3KeyGenerator
 * 这里必须用继承
 *
 */
public class MyJdbc3KeyGenerator extends Jdbc3KeyGenerator{
	@Override
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		super.processBefore(executor, ms, stmt, parameter);
	}
	@Override
	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		Object origParamObject = ParamObjectHolder.get();
		if(origParamObject!=null){
			parameter = origParamObject;
		}
		super.processAfter(executor, ms, stmt, parameter);
	}
}
