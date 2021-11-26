package com.andyadc.codeblocks.test.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

/**
 * https://programmer.group/apache-commons-dbutils-integrates-spring-framework-to-realize-simple-crud.html
 */
public class DbUtilsTests {

	private static HikariDataSource dataSource;

	@BeforeAll
	public static void startup() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3307/idea"
			+ "?useSSL=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8"
		);
		dataSource.setUsername("root");
		dataSource.setPassword("andyadc");
	}

	@AfterAll
	public static void windup() {
		dataSource.close();
	}

	@Test
	public void testQueryRunner() {
		QueryRunner queryRunner = new QueryRunner(dataSource);

		try {
			String sql = "insert into demo (name) values ('" + UUID.randomUUID().toString() + "')";
			queryRunner.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTransaction() throws Exception {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);

		try {
			queryRunner.update(
				connection,
				"update demo set name = '" + UUID.randomUUID().toString() + "' where id = ?",
				1
			);

			int i = 1 / 0;

			queryRunner.update(
				connection,
				"update demo set name = '" + UUID.randomUUID().toString() + "' where id = ?",
				2
			);

			DbUtils.commitAndCloseQuietly(connection);
		} catch (Exception e) {
			DbUtils.rollbackAndCloseQuietly(connection);
			e.printStackTrace();
		}
	}
}
