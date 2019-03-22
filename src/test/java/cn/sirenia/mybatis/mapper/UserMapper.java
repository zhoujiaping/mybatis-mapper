package cn.sirenia.mybatis.mapper;

import java.util.List;
import java.util.Map;

import cn.sirenia.mybatis.anno.Table;
import cn.sirenia.mybatis.model.User;
import cn.sirenia.mybatis.plugin.model.Example;
@Table(name="sys_user")
public interface UserMapper extends BaseMapper<User, Example, Integer>{
	List<User> selectByName(String name);
	List<User> selectByMap(Map<String,Object> map);
	List<User> selectByUser(User u);
}