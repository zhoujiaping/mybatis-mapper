package cn.zhou.mapper.dao;

import cn.zhou.mapper.anno.Table;
import cn.zhou.mapper.model.User;
import cn.zhou.mapper.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User,UserExample,Integer>{

}