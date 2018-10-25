package cn.sirenia.mybatis.mapper;

import cn.sirenia.mybatis.anno.Table;
import cn.sirenia.mybatis.model.User;
import cn.sirenia.mybatis.plugin.model.Example;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User, Example, Integer>{
}