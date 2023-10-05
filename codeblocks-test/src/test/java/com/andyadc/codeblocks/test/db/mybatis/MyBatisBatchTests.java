package com.andyadc.codeblocks.test.db.mybatis;

import com.andyadc.codeblocks.test.db.batch.mybatis.User;
import com.andyadc.codeblocks.test.db.batch.mybatis.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyBatisBatchTests {

	@Test
	public void testBatchInsertUser() throws Exception {
		InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sessionFactory.openSession();
		System.out.println(sqlSession.toString());
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);

		try {
			List<User> userList = new ArrayList<>(100000);
			for (int i = 0; i < 1000; i++) {
				User user = new User();
				user.setAge(i % 3);
				user.setName("u_" + i);
				userList.add(user);
			}
			long start = System.currentTimeMillis();
			mapper.batchInsertUser(userList);
			sqlSession.commit();
			System.out.println("Elapsed time=" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}


}
