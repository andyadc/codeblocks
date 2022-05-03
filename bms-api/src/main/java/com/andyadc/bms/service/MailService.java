package com.andyadc.bms.service;

import com.andyadc.codeblocks.kit.text.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
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
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	private final SpringTemplateEngine templateEngine;
	private final ITemplateResolver templateResolver;
	private JavaMailSender mailSender;
	private MessageSource messageSource;

	@Value("${spring.mail.username}")
	private String mailFrom;
	@Value("classpath:static/images/divider.png")
	private Resource resourceFile;

	public MailService() {
		templateResolver = mailTemplateResolver();
		templateEngine = thymeleafTemplateEngine();
	}

	@Inject
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Inject
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public boolean sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		Instant begin = Instant.now();
		try {
			mailMessage.setTo(to);
			mailMessage.setFrom(mailFrom);
			mailMessage.setSubject(subject);
			mailMessage.setText(text);
			mailSender.send(mailMessage);
			return true;
		} catch (Exception e) {
			logger.error("SendSimpleMessage error.", e);
		} finally {
			Instant end = Instant.now();
			logger.info("SendSimpleMessage elapsed time: {} ms", Duration.between(begin, end).toMillis());
		}
		return false;
	}

	public void sendComplexMail(String to, String subject, String text,
								boolean isHtmlMail,
								Map<String, String> inlineImageMap,
								List<String> filePathList) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true = multipart
		helper.setFrom(mailFrom);
		helper.setTo(to);
		helper.setSubject(subject);
		if (isHtmlMail) {
			helper.setText(text, true); // true = isHtml
		} else {
			helper.setText(text);
		}

		// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
		if (inlineImageMap != null && inlineImageMap.size() > 0) {
			for (Map.Entry<String, String> entry : inlineImageMap.entrySet()) {
				FileSystemResource resource = new FileSystemResource(new File(entry.getValue()));
				helper.addInline(entry.getKey(), resource);
			}
		}

		if (filePathList != null && filePathList.size() > 0) {
			for (String path : filePathList) {
				if (StringUtil.isBlank(path)) {
					continue;
				}
				FileSystemResource resource = new FileSystemResource(new File(path));
				String filename = MimeUtility.encodeWord(resource.getFilename(), "utf-8", "B");
				helper.addAttachment(filename, resource.getFile());
			}
		}

		mailSender.send(message);
		String messageID = message.getMessageID();
		logger.info("Message-ID: {}", messageID);
	}

	public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException, IOException {
		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);

//		String htmlBody = templateEngine.process("email-template.html", thymeleafContext);
//		String htmlBody = templateEngine.process("verfication-code-mail", thymeleafContext);
		String htmlBody = templateEngine.process("registration-confirm", thymeleafContext);

		sendHtmlMessage(to, subject, htmlBody);
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // true = multipart

		helper.setFrom(mailFrom);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true); // true = isHtml
		helper.addInline("divider.png", resourceFile);

		Resource resource = new ClassPathResource("static/images/tiger.jpg");
		String attachmentFilename = MimeUtility.encodeWord(resource.getFilename(), "utf-8", "B");
		helper.addAttachment(attachmentFilename, resource.getFile());

		mailSender.send(message);
		String messageID = message.getMessageID();
		logger.info("Message-ID: {}", messageID);
	}

	private SpringTemplateEngine thymeleafTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.setTemplateEngineMessageSource(messageSource);
		return templateEngine;
	}

	private ITemplateResolver mailTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/mail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(2);
		return templateResolver;
	}
}
