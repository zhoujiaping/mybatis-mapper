package cn.howso.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.howso.mybatis.mapper.UserMapper;
import cn.howso.mybatis.model.User;

public class Index {
    public static void main(String[] args) throws IOException {
        String resource = "configuration.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        //XMLConfigBuilder xMLConfigBuilder = new XMLConfigBuilder(reader);
        //Configuration configuration = xMLConfigBuilder.parse();
        //SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        //根据configuartion.xml创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //打开session
        SqlSession session = sqlSessionFactory.openSession();
        //基于statementId的接口
        User user = (User)session.selectOne("cn.howso.mybatis.mapper.UserMapper.selectByPrimaryKey", 1L);
        System.out.println(user.getName());
        //基于Mapper的接口
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user2 = userMapper.selectByPrimaryKey(1L);
        System.out.println(user2.getName());
    }
}
