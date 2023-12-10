package org.zju.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.zju.pojo.User;
import org.zju.mapper.userMapper;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public static SqlSessionFactory build() throws IOException {
        String resource = "Mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test() throws IOException {
        String resource = "Mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        userMapper users = sqlSession.getMapper(userMapper.class);
        /*List<User> selectall = users.selectall();
        System.out.println(selectall);*/
    }
}
