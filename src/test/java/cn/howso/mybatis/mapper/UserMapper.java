package cn.howso.mybatis.mapper;

import cn.howso.mybatis.anno.Table;
import cn.howso.mybatis.model.Example;
import cn.howso.mybatis.model.User;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User, Example, Long>{
}