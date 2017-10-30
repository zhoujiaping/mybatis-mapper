package cn.howso.mybatis;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.howso.mybatis.mapper.UserMapper;
import cn.howso.mybatis.model.Example;
import cn.howso.mybatis.model.IndexPage;
import cn.howso.mybatis.model.PageRes;
import cn.howso.mybatis.model.User;

public class MybatisTest {

    private static String resource = "configuration.xml";
    private static SqlSessionFactory sqlSessionFactory;

    //@BeforeClass
    public static void beforeClass() throws IOException {
        /*
         * 1、建库，执行init-test.sql 2、执行mybatis-generator:generate，拷贝文件到项目对应位置 3、执行测试
         */
        Reader reader = Resources.getResourceAsReader(resource);
        XMLConfigBuilder xMConfigBuilder = new XMLConfigBuilder(reader);
        Configuration configuration = xMConfigBuilder.parse();
        sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        // SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    //@AfterClass
    public static void afterClass() {
    }

    //@Test
    public void testCountByExample() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        example.createCriteria().and("name").like("%avril%");
        int count = userMapper.countByExample(example);
        Assert.assertEquals(count, 1);
        // session.commit();不提交，使得每个测试单独不影响
        session.rollback();
        session.close();
    }

    //@Test
    public void deleteByExample() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        example.createCriteria().and("name").like("%avril%");
        int count = userMapper.deleteByExample(example);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void insert() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User record = new User();
        record.setId(-1);
        record.setName("xiaodao");
        record.setPassword("123456");
        int count = userMapper.insert(record);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void insertSelective() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User record = new User();
        record.setId(-1);
        record.setName("xiaodao");
        int count = userMapper.insertSelective(record);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void selectByExample() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        List<User> users = new ArrayList<>();
        ResultHandler<Map<String,Object>> handler = new ResultHandler<Map<String,Object>>() {
			//@Override
			public void handleResult(ResultContext<? extends Map<String,Object>> resultContext) {
				Map<String,Object> res = resultContext.getResultObject();
				/*res.forEach(item->{
					User user = new User();
					user.setId((ong)item.get("id"));
					user.setName((String)item.get("name"));
					user.setNick((String)item.get("nick"));
					user.setPassword((String)item.get("password"));
					users.add(user);
				});*/
				User user = new User();
				user.setId((Integer)res.get("id"));
				user.setName((String)res.get("name"));
				user.setNick((String)res.get("nick"));
				user.setPassword((String)res.get("password"));
				users.add(user);
			}
		};
		List<?> haha = session.selectList("cn.howso.mybatis.mapper.UserMapper.test",example);
		//session.select("cn.howso.mybatis.mapper.UserMapper.test",example, handler );
		System.out.println(haha);
		System.out.println(users);
        example.createCriteria().and("name").like("%zhou%");
        //ist<User> users = userMapper.selectByExample(example);
        //Assert.assertEquals(users.size(), 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void updateByExampleSelective() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        example.createCriteria().and("name").like("%avril%");
        User record = new User();
        record.setName("xiaodao");
        int count = userMapper.updateByExampleSelective(record,example);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void updateByExample() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        example.createCriteria().and("name").like("%avril%");
        User record = new User();
        record.setId(-2);
        record.setName("xiaodao");
        int count = userMapper.updateByExample(record,example);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void selectByExampleByPage() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Example example = new Example();
        example.createCriteria().and("name").notLike("%avril%");
        IndexPage page = IndexPage.of(1, 3);
        List<User> rows = userMapper.selectByExampleByPage(example,page);
        PageRes<User> pageRes = PageRes.of(page, rows);
        Assert.assertEquals(pageRes.getTotal().intValue(), 4);
        Assert.assertEquals(pageRes.getRows().size(), 3);
        session.rollback();
        session.close();
    }

    //@Test
    public void batchInsert() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> userist = new ArrayList<>();
        User user1 = new User();
        user1.setId(-1);
        User user2 = new User();
        user2.setId(-2);
        User user3 = new User();
        user3.setId(-3);
        userist.add(user1);
        userist.add(user2);
        userist.add(user3);
        int count = userMapper.batchInsert(userist);
        Assert.assertEquals(count, 3);
        session.rollback();
        session.close();
    }

    //@Test
    public void batchInsertSelective() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> userist = new ArrayList<>();
        User user1 = new User();
        user1.setId(-1);
        User user2 = new User();
        user2.setId(-2);
        User user3 = new User();
        user3.setNick("qiaofeng");
        user3.setId(-3);
        userist.add(user1);
        userist.add(user2);
        userist.add(user3);
        int count = userMapper.batchInsertSelective(userist);
        Assert.assertEquals(count, 3);
        session.rollback();
        session.close();
    }
    
    //@Test
    public void deleteByPrimaryKey() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int count = userMapper.deleteByPrimaryKey(1);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void selectByPrimaryKey() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(1);
        Assert.assertEquals(user.getId().longValue(), 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void updateByPrimaryKeySelective() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = new User();
        user.setId(1);
        user.setNick("fuck");
        int count = userMapper.updateByPrimaryKeySelective(user);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    //@Test
    public void updateByPrimaryKey() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = new User();
        user.setId(1);
        user.setNick("fuck");
        int count = userMapper.updateByPrimaryKey(user);
        Assert.assertEquals(count, 1);
        session.rollback();
        session.close();
    }

    /*//@Test
    public void insertSelectiveSelectKey() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = new User();
        user.setNick("fuck");
        int count = userMapper.insertSelectiveSelectKey(user);
        Assert.assertEquals(count, 1);
        Assert.assertEquals(user.getId().intValue(), 6);
    }*/
    //@Test
    public void selectKey() {
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Integer id = userMapper.selectKey();
        Assert.assertTrue(id>5);
        session.rollback();
        session.close();
    }
}
