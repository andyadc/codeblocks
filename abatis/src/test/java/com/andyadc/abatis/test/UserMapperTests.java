package com.andyadc.abatis.test;

import com.andyadc.abatis.binding.MapperProxyFactory;
import com.andyadc.abatis.test.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserMapperTests {

	@Test
	public void test_MapperProxyFactory() {
		MapperProxyFactory<UserMapper> factory = new MapperProxyFactory<>(UserMapper.class);
		Map<String, String> sqlSession = new HashMap<>();

		sqlSession.put("com.andyadc.abatis.test.mapper.UserMapper.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
		sqlSession.put("com.andyadc.abatis.test.mapper.UserMapper.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");

//		UserMapper userMapper = factory.newInstance(sqlSession);
//		System.out.println(userMapper);
//
//		String userName = userMapper.queryUserName("10001");
//		System.out.println(userName);
	}
}
