package cn.sirenia.mybatis.keygen;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import cn.sirenia.mybatis.util.ParamObjectHolder;
/**
 * 用来包装SelectKeyGenerator
 * 这里必须用装饰器模式，不能用继承。。。我晕！！！
 *
 */
public class KeyGeneratorWrapper implements KeyGenerator{
	private SelectKeyGenerator keyGenerator;
	public KeyGeneratorWrapper(SelectKeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	@Override
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		keyGenerator.processBefore(executor, ms, stmt, parameter);
	}
	@Override
	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		Object origParamObject = ParamObjectHolder.get();
		if(origParamObject!=null){
			parameter = origParamObject;
		}
		keyGenerator.processAfter(executor, ms, stmt, parameter);
	}
}
