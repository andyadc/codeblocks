package com.andyadc.bms.test;

import com.andyadc.bms.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * https://blog.csdn.net/Monten_Cristo/article/details/117464187
 */
@SpringBootTest
public class MailTests {

	@Inject
	private JavaMailSender javaMailSender;
	@Inject
	private MailService mailService;

	@Test
	public void testStore() throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.imap.auth", "true");
		props.setProperty("mail.imap.timeout", "300000");
		props.setProperty("mail.imap.connectiontimeout", "300000");
		props.setProperty("mail.imap.writetimeout", "300000");
		props.setProperty("mail.imap.partialfetch", "false");
		props.setProperty("mail.imaps.partialfetch", "false");


		props.setProperty("mail.imap.host", "imap.gmail.com");
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imap.port", "993");
		props.setProperty("mail.imap.socketFactory.port", "993");

		Session session = Session.getDefaultInstance(props, null);

		URLName urlName = new URLName(
			"imap",
			"imap.gmail.com",
			993,
			null,
			"xxx9@gmail.com",
			"xxxpassword"
		);

		Store store = session.getStore(urlName);
		store.connect();
		store.close();
	}

	@Test
	public void testSimpleSend() {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("test");
		mailMessage.setText("hello mail, " + LocalDateTime.now());
		mailMessage.setTo("andaicheng@qq.com");
		mailMessage.setFrom("andaicheng@163.com");
		javaMailSender.send(mailMessage);
	}

	@Test
	public void testSendHtmlMail() throws Exception {
		String to = "andaicheng@gmail.com";
		String subject = "Mime mail 1";
		Map<String, Object> data = new HashMap<>();
		data.put("recipientName", "andyadc");
		data.put("senderName", "adc");
		data.put("text", "This mime mail");
		mailService.sendMessageUsingThymeleafTemplate(to, subject, data);
	}
}
