package cn.sirenia.mybatis.util;

public class ParamObjectHolder {
	private static final ThreadLocal<Object> paramObjectTl = new InheritableThreadLocal<>();
	public static void set(Object paramObject){
		paramObjectTl.set(paramObject);
	}
	public static Object get(){
		return paramObjectTl.get();
	}
	public static void remove(){
		paramObjectTl.remove();
	}

}
