package com.andyadc.codeblocks.test.flyway;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class FlywayTests {

	public static void main(String[] args) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setUsername("root");
		hikariConfig.setPassword("andyadc");
		hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8");
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariConfig.setMaximumPoolSize(3);
		DataSource dataSource = new HikariDataSource(hikariConfig);

//		Flyway flyway = Flyway.configure().dataSource(dataSource).load();
//		flyway.migrate();
	}
}
