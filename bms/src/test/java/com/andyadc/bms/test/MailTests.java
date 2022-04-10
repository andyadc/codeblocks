package com.andyadc.bms.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.inject.Inject;

@SpringBootTest
public class MailTests {

	@Inject
	private JavaMailSender javaMailSender;

	@Test
	public void testSend() {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("test");
		mailMessage.setText("hello mail");
		mailMessage.setTo("andaicheng@qq.com");
		mailMessage.setFrom("andaicheng@163.com");
		javaMailSender.send(mailMessage);
	}
}
