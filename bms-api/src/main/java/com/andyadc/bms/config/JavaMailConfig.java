package com.andyadc.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfig {

	//	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		Properties properties = new Properties();
		properties.setProperty("mail.mime.splitlongparameters", "false");

		mailSender.setJavaMailProperties(properties);
		return mailSender;
	}
}
