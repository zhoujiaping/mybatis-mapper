package cn.zhou.mapper.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.zhou.mapper.model.Label;
import cn.zhou.mapper.model.LabelExample;

public interface LabelMapper {
    int countByExample(LabelExample example);

    int deleteByExample(LabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Label record);

    int insertSelective(Label record);

    List<Label> selectByExample(LabelExample example);

    Label selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Label record, @Param("example") LabelExample example);

    int updateByExample(@Param("record") Label record, @Param("example") LabelExample example);

    int updateByPrimaryKeySelective(Label record);

    int updateByPrimaryKey(Label record);

    Integer insertAndReturnId(Label label);
}