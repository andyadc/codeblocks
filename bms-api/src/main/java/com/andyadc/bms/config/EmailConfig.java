package com.andyadc.bms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

	@Value("${spring.mail.protocol}")
	private String mailServerProtocol;

	@Value("${spring.mail.host}")
	private String mailServerHost;

//	@Value("${spring.mail.port}")
//	private Integer mailServerPort;

	@Value("${spring.mail.username}")
	private String mailServerUsername;

	@Value("${spring.mail.password}")
	private String mailServerPassword;


	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailServerHost);
		mailSender.setUsername(mailServerUsername);
		mailSender.setPassword(mailServerPassword);
		mailSender.setProtocol(mailServerProtocol);
//		mailSender.setPort(465);

		Properties props = mailSender.getJavaMailProperties();
		props.setProperty("mail.debug", "true");
		props.setProperty("mail.mime.splitlongparameters", "false");
//		props.setProperty("mail.transport.protocol", "smtp");
//		props.setProperty("mail.smtp.auth", true);
//		props.setProperty("mail.smtp.starttls.enable", false);

		return mailSender;
	}
}
