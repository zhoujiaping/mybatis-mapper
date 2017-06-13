package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.dao.BaseMapper;
import cn.zhou.mapper.model.EciLocation;
import cn.zhou.mapper.model.EciLocationExample;
@Table(name="sm_eci_location")
public interface EciLocationMapper extends BaseMapper<EciLocation, EciLocationExample, Long>{
}