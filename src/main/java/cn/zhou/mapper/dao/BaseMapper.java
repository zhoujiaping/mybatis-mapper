package cn.zhou.mapper.dao;

/**
 * 通用mapper接口，子接口只要继承该接口并添加一个说明表名的注解，
 * 即可获得单表增、删、查、改、批量增删改、分页查、按example查等多种方法。
 * */
public interface BaseMapper<ENTITY, EXAMPLE, PK> extends ExampleMapper<ENTITY, EXAMPLE>,PrimaryKeyMapper<ENTITY, PK>{
	
}
