package com.andyadc.bms.security.configurers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
