package com.andyadc.codeblocks.test.db.mybatis;

import com.andyadc.codeblocks.common.JsonUtils;
import com.andyadc.codeblocks.test.db.oracle.BalanceOutput;
import com.andyadc.codeblocks.test.db.oracle.OracleMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatisOracleTests {

	@Test
	public void test_selectTmp() throws Exception {
		InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-oracle-config.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sessionFactory.openSession();
		System.out.println("sqlSession: [" + sqlSession.toString() + "]");
		OracleMapper mapper = sqlSession.getMapper(OracleMapper.class);

		try {
			long start = System.currentTimeMillis();

			List<Map<String, Object>> mapList = mapper.selectTmp();
			System.out.println(JsonUtils.toJSONString(mapList));

			sqlSession.commit();
			System.out.println("Elapsed time=" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test_call_procedure() throws Exception {
		InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-oracle-config.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sessionFactory.openSession();
		System.out.println("sqlSession: [" + sqlSession.toString() + "]");
		OracleMapper mapper = sqlSession.getMapper(OracleMapper.class);

		try {
			long start = System.currentTimeMillis();

			LocalDate localDate1 = LocalDate.of(2025, 3, 1);
			Date date1 = Date.from(localDate1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

			LocalDate localDate2 = LocalDate.of(2025, 3, 10);
			Date date2 = Date.from(localDate2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

			Map<String, Object> params = new HashMap<>();
			params.put("V_ACCTCODE", 1001148403001L);
			params.put("V_START_POSTDATE", date1);
			params.put("V_END_POSTDATE", date2);
			params.put("V_START_HOUR", 0);
			params.put("V_END_HOUR", 23);
			params.put("V_PAGESIZE", 10);
			params.put("V_NEXTPAGE", 1);
			params.put("V_MAX_POSTDATE", "2024-03-06 10:11:08");
			params.put("V_BALANCE", new BigDecimal("1000"));

			List<BalanceOutput> balanceList = mapper.callBalanceList(params);
			System.out.println(JsonUtils.toJSONString(balanceList));

			sqlSession.commit();
			System.out.println("Elapsed time=" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}


}
