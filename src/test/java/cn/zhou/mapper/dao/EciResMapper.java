package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.dao.BaseMapper;
import cn.zhou.mapper.model.EciRes;
import cn.zhou.mapper.model.EciResExample;
@Table(name="sy_eci_res")
public interface EciResMapper extends BaseMapper<EciRes, EciResExample, Integer>{
}