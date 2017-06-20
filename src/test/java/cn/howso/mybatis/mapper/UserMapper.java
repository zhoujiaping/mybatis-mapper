package cn.howso.mybatis.mapper;

import cn.howso.mybatis.anno.Table;
import cn.howso.mybatis.model.User;
import cn.howso.mybatis.model.UserExample;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User, UserExample, Long>{
}