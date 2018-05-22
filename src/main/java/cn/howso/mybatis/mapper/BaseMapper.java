package cn.howso.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.howso.mybatis.model.Pageable;

/**
 * 通用mapper接口，子接口只要继承该接口并添加一个说明表名的注解， 即可获得单表增、删、查、改、批量增删改、分页查、按example查等多种方法。
 */
public interface BaseMapper<ENTITY, EXAMPLE, PK>{

    @ResultType(Integer.class)
    @Select("howso-countByExample")
    int countByExample(EXAMPLE example);

    @ResultType(Integer.class)
    @Delete("howso-deleteByExample")
    int deleteByExample(EXAMPLE example);

    @ResultType(Integer.class)
    @Insert("howso-insert")
    int insert(ENTITY record);

    @ResultType(Integer.class)
    @Insert("howso-insertSelective")
    int insertSelective(ENTITY record);

    @ResultMap("BaseResultMap")
    @Select("howso-selectByExample")
    List<ENTITY> selectByExample(EXAMPLE example);
    
    @ResultMap("BaseResultMap")
    @Select("howso-selectUniqueByExample")
    ENTITY selectUniqueByExample(EXAMPLE example);

    @ResultType(Integer.class)
    @Update("howso-updateByExampleSelective")
    int updateByExampleSelective(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

    @ResultType(Integer.class)
    @Update("howso-updateByExample")
    int updateByExample(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

    @ResultMap("BaseResultMap")
    @Select("howso-selectByExampleByPage")
    List<ENTITY> selectByExampleByPage(@Param("example") EXAMPLE example, @Param("page") Pageable page);

    @ResultType(Integer.class)
    @Insert("howso-batchInsert")
    int batchInsert(@Param("recordList") List<ENTITY> recordList);

    @ResultType(Integer.class)
    @Insert("howso-batchInsertSelective")
    int batchInsertSelective(@Param("recordList") List<ENTITY> recordList);

    @ResultType(Integer.class)
    @Delete("howso-deleteByPrimaryKey")
    int deleteByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。
    @ResultType(Integer.class)
    @Delete("howso-deleteByPrimaryKeyAndOptiVersion")
    int deleteByPrimaryKeyAndOptiVersion(@Param("id") PK id,@Param("optiVersion")long optiVersion);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。

    @ResultMap("BaseResultMap")
    @Select("howso-selectByPrimaryKey")
    ENTITY selectByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。

    @ResultType(Integer.class)
    @Update("howso-updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ENTITY record);
    @ResultType(Integer.class)
    @Update("howso-updateByPrimaryKeyAndOptiVersionSelective")
    int updateByPrimaryKeyAndOptiVersionSelective(ENTITY record);

    @ResultType(Integer.class)
    @Update("howso-updateByPrimaryKey")
    int updateByPrimaryKey(ENTITY record);
    @ResultType(Integer.class)
    @Update("howso-updateByPrimaryKeyAndOptiVersion")
    int updateByPrimaryKeyAndOptiVersion(ENTITY record);
    /*@ResultType(Integer.class)
    @SelectKey(before=true,keyColumn="id",resultType=Integer.class,statement="select nextVal('sys_user_id_seq')", keyProperty = "id")
    @Insert("howso-insertSelectiveSelectKey")
    int insertSelectiveSelectKey(ENTITY record);*/
    /**
     * 比在xml中配置<selectKey></selectKey>更易理解
     * @param <T>
     * */
    @ResultType(Object.class)
    @Select("howso-selectKey")
    PK selectKey();
}
