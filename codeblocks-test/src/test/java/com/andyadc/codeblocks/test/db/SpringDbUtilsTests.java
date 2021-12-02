package com.andyadc.codeblocks.test.db;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

/**
 * https://zhuanlan.zhihu.com/p/89233246
 * TODO
 * HikariCP: Executed rollback on connection {} due to dirty commit state on close()
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/spring-dbutils.xml"})
public class SpringDbUtilsTests {

	@Autowired
	private QueryRunner queryRunner;
	@Autowired
	private DbUtilsTemplate dbUtilsTemplate;

	/**
	 *
	 */
	@Test
	public void testUpdateByTemplate() {
		for (int i = 0; i < 1; i++) {
			String sql = "insert into demo (name) values ('" + UUID.randomUUID().toString() + "')";
			dbUtilsTemplate.update(sql);
		}
	}

	@Test
	public void testQueryByTemplate() {
		String sql = "select * from demo";
		dbUtilsTemplate.query(sql);
	}

	/**
	 * HikariCP
	 */
//	@Transactional
	@Test
	public void testUpdate() throws Exception {
		String sql = "insert into demo (name) values ('" + UUID.randomUUID().toString() + "')";
		queryRunner.update(sql);
	}
}
