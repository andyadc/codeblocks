package com.andyadc.codeblocks.test.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiConfig {

	@Bean
	public Jdbi jdbi(DataSource dataSource) {
		// Create and configure JDBI instance
		return Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
	}

}
