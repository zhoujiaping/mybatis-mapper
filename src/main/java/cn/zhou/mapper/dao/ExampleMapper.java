package cn.zhou.mapper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.zhou.mapper.page.Pageable;

/**
 * 如果定义的表没有主键，则不需要关于主键的方法，那么继承这个接口更合适
 */
public interface ExampleMapper<ENTITY, EXAMPLE> {
	@ResultType(Long.class)
	@Select("z-countByExample")
    int countByExample(EXAMPLE example);

    int deleteByExample(EXAMPLE example);

    int insert(ENTITY record);

    int insertSelective(ENTITY record);
    @ResultMap("BaseResultMap")
    @Select("z-selectByExample")
    List<ENTITY> selectByExample(EXAMPLE example);

    int updateByExampleSelective(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

    int updateByExample(@Param("record") ENTITY record, @Param("example") EXAMPLE example);

    List<ENTITY> selectByExampleByPage(@Param("example") EXAMPLE example, @Param("page") Pageable page);

    int batchInsert(@Param("recordList") List<ENTITY> recordList);

    int batchInsertSelective(@Param("recordList") List<ENTITY> recordList);
}
