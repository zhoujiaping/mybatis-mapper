package cn.sirenia.mybatis.mapper;

import java.util.List;

import cn.sirenia.mybatis.model.User;
import cn.sirenia.mybatis.plugin.model.Example;
import cn.sirenia.mybatis.plugin.model.Pageable;

public class UserMapperImpl implements UserMapper {

	@Override
	public int countByExample(Example example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByExample(Example example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> selectByExample(Example example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectUniqueByExample(Example example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByExampleSelective(User record, Example example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByExample(User record, Example example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> selectByExampleByPage(Example example, Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int batchInsert(List<User> recordList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int batchInsertSelective(List<User> recordList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKeyAndVersion(Integer id, Integer version) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
		System.out.println(id);
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKeySelectiveAndVersion(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKeyAndVersion(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer selectKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
