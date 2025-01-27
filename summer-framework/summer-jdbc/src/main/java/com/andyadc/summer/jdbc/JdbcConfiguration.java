package com.andyadc.summer.jdbc;

import com.andyadc.summer.annotation.Bean;
import com.andyadc.summer.annotation.Configuration;
import com.andyadc.summer.annotation.Value;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

	@Bean(destroyMethod = "close")
	DataSource dataSource(
		// properties:
		@Value("${summer.datasource.url}") String url, //
		@Value("${summer.datasource.username}") String username, //
		@Value("${summer.datasource.password}") String password, //
		@Value("${summer.datasource.driver-class-name:}") String driver, //
		@Value("${summer.datasource.maximum-pool-size:20}") int maximumPoolSize, //
		@Value("${summer.datasource.minimum-pool-size:1}") int minimumPoolSize, //
		@Value("${summer.datasource.connection-timeout:30000}") int connTimeout //
	) {
		HikariConfig config = new HikariConfig();
		config.setAutoCommit(false);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		if (driver != null) {
			config.setDriverClassName(driver);
		}
		config.setMaximumPoolSize(maximumPoolSize);
		config.setMinimumIdle(minimumPoolSize);
		config.setConnectionTimeout(connTimeout);
		return new HikariDataSource(config);
	}

}