package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.dao.BaseMapper;
import cn.zhou.mapper.model.Area;
import cn.zhou.mapper.model.AreaExample;
@Table(name="sy_area")
public interface AreaMapper extends BaseMapper<Area, AreaExample, Integer> {
}