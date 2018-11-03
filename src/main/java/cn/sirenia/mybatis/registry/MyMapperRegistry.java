package cn.sirenia.mybatis.registry;

import java.lang.reflect.Method;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import cn.sirenia.mybatis.util.ReflectHelper;

public class MyMapperRegistry extends MapperRegistry {
	private Object beanHolder;//比如spring的applicationContext
	private Method getBeanMethod;//將方法緩存起來，保證只有第一次使用反射
	public MyMapperRegistry(Configuration config) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(config);
	}
	  @SuppressWarnings("unchecked")
	  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		  try {
			  if(getBeanMethod==null){
				  getBeanMethod = beanHolder.getClass().getMethod("getBean", type.getClass());
			  }
			return (T)getBeanMethod.invoke(beanHolder, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	  }
	  public void setBeanHolder(Object holder){
		  beanHolder = holder;
	  }
	  public static void replaceConfigMapperRegistry(Configuration config) throws Exception{
	        ReflectHelper.setValueByFieldName(config, "mapperRegistry", new MyMapperRegistry(config));
	  }
}
