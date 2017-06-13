package cn.zhou.mapper.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 通常不直接使用该接口
 */
public interface PrimaryKeyMapper<ENTITY, PK> {

    int deleteByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。

    ENTITY selectByPrimaryKey(@Param("id") PK id);// @Param("id")不要删除，约定名称为传参名为id，这样简单方便。

    int updateByPrimaryKeySelective(ENTITY record);

    int updateByPrimaryKey(ENTITY record);

    int insertSelectiveSelectKey(ENTITY record);
}
