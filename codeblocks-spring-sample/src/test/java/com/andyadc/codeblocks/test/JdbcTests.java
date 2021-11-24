package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcTests {

	@Test
	public void testBatchInsert() throws Exception {
		Connection connection = DriverManager.getConnection(
			"jdbc:mysql://localhost:3307/idea" +
				"?useSSL=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8",
			"root",
			"andyadc"
		);

		connection.setAutoCommit(false);

		PreparedStatement ps = connection.prepareStatement(
			"insert into spring_transaction (name) values (?)"
		);

		for (int i = 0; i < 100; i++) {
			ps.setString(1, i + "");
			ps.addBatch();
		}

		ps.executeBatch();
		connection.commit();
		connection.close();
	}
}
