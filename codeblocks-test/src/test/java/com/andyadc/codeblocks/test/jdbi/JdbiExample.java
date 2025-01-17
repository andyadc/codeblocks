package com.andyadc.codeblocks.test.jdbi;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

/**
 * https://jdbi.org/releases/3.39.1/
 * https://jdbi.org/#_release_documentation
 */
public class JdbiExample {

	static MysqlDataSource buildDataSource() {
		// Configure MySQL DataSource
		MysqlDataSource mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setURL("jdbc:mysql://localhost:3306/idea?useSSL=false");
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("andyadc");
		return mysqlDataSource;
	}

	@Test
	public void test() {
		// Create JDBI instance
		Jdbi jdbi = Jdbi.create(buildDataSource());

		// Use JDBI
		jdbi.useHandle(handle -> {
			handle.execute("CREATE TABLE IF NOT EXISTS jdbi_users (id INT PRIMARY KEY, name VARCHAR(50))");
			handle.execute("INSERT INTO jdbi_users (id, name) VALUES (?, ?)", 1, "Alice");
			handle.execute("INSERT INTO jdbi_users (id, name) VALUES (?, ?)", 2, "Bob");
			handle.execute("INSERT INTO jdbi_users (id, name) VALUES (?, ?)", 3, "Andy");

			handle.createQuery("SELECT name FROM jdbi_users")
				.mapTo(String.class)
				.list()
				.forEach(System.out::println);

		});

	}

	@Test
	public void testUserJdbiDao() {
		// Create JDBI instance and register SqlObjectPlugin
		Jdbi jdbi = Jdbi.create(buildDataSource()).installPlugin(new SqlObjectPlugin());

		jdbi.useHandle(handle -> {
			UserJdbiDao dao = handle.attach(UserJdbiDao.class);

			// Create table
			dao.createTable();

			// Insert users
			dao.insertNamed(4, "Green");
			dao.insertNamed(5, "Lily");

			// Query user
			User user = dao.getUser(5);
			System.out.println(user.getName()); // Output: Lily
		});

	}

	@Test
	public void testUseTransaction() {
		Jdbi jdbi = Jdbi.create(buildDataSource());

		jdbi.useTransaction(handle -> {
			handle.execute("INSERT INTO jdbi_users (id, name) VALUES (?, ?)", 6, "Charlie");
			handle.execute("INSERT INTO jdbi_users (id, name) VALUES (?, ?)", 7, "David");
		});

	}

	@Test
	public void testReturnGeneratedKey() {
		Jdbi jdbi = Jdbi.create(buildDataSource());

		jdbi.useTransaction(handle -> {
			handle.createUpdate("INSERT INTO jdbi_users (name) VALUES (:name)")
				.bind("name", "Memo")
				.executeAndReturnGeneratedKeys("id")
				.mapTo(Long.class)
				.one(); // Retrieve the generated key
		});

	}

	@Test
	public void testUseConnectionPool() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/idea");
		config.setUsername("root");
		config.setPassword("andyadc");

		DataSource dataSource = new HikariDataSource(config);
		Jdbi jdbi = Jdbi.create(dataSource);

		jdbi.useHandle(handle -> {

		});

	}


}
