package com.andyadc.codeblocks.test.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcTests {

	@Test
	public void testJDBCBatchInsertUser() throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String url = "jdbc:mysql://localhost:3306/idea";
		String name = "root";
		String password = "andyadc";

		try {
			connection = DriverManager.getConnection(url, name, password);
			connection.setAutoCommit(false);

			long start = System.currentTimeMillis();

			String batchSql = "INSERT INTO `user` (name, age) VALUES (?, ?)";
			preparedStatement = connection.prepareStatement(batchSql);

			for (int i = 0; i < 1001; i++) {
				preparedStatement.setString(1, "u_" + i);
				preparedStatement.setInt(2, i % 3);

				// 添加到批处理中
				preparedStatement.addBatch();

				// 每1000条数据提交一次
				if (i % 1000 == 0) {
					preparedStatement.executeBatch();
					connection.commit();
				}
			}

			// 处理剩余的数据
			preparedStatement.executeBatch();
			connection.commit();

			System.out.println("Elapsed time=" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}
}
