package com.andyadc.bms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Map;

@Service
public class MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	private JavaMailSender mailSender;
	private MessageSource messageSource;

	@Value("${spring.mail.username}")
	private String mailFrom;

	@Value("classpath:static/images/robot.png")
	private Resource resourceFile;

	@Inject
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Inject
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setFrom(mailFrom);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		helper.addInline("attachment.png", resourceFile);

		Resource resource = new ClassPathResource("static/images/tiger.jpg");
		String attachmentFilename = MimeUtility.encodeWord(resource.getFilename(), "utf-8", "B");
		helper.addAttachment(attachmentFilename, resource.getFile());

		mailSender.send(message);
		String messageID = message.getMessageID();
		System.out.println("messageID: " + messageID);
	}

	public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException, IOException {
		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);

		String htmlBody = thymeleafTemplateEngine().process("email-template.html", thymeleafContext);

		sendHtmlMessage(to, subject, htmlBody);
	}

	public SpringTemplateEngine thymeleafTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(mailTemplateResolver());
		templateEngine.setTemplateEngineMessageSource(messageSource);

		return templateEngine;
	}

	public ITemplateResolver mailTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/mail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(2);
		return templateResolver;
	}
}
