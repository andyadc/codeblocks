package com.andyadc.bms.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Profile("test")
@Configuration(proxyBeanMethods = false)
public class UserDetailsServiceConfiguration {

	@Bean
	UserDetailsService userDetailsService() {
		return username ->
			User.withUsername(username)
				.password("123465")
				.authorities("ROLE_USER", "ROLE_ADMIN")
				.build();
	}
}
