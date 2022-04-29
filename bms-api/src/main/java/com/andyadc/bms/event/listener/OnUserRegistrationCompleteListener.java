package com.andyadc.bms.event.listener;

import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.Duration;
import java.util.UUID;

@Order(1)
@Component
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationCompleteListener.class);

	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mailFrom;
	@Value("${mail.send.mock}")
	private Boolean mailSendMock;

	//TODO
	@Async
	@Override
	public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnUserRegistrationCompleteEvent event) {
		AuthUser authUser = event.getAuthUser();
		logger.info("Send mail after user registration. {}", authUser.getId() + "-" + authUser.getUsername());
		String email = authUser.getEmail();
		if (StringUtil.isBlank(email)) {
			logger.warn("User email is null.");
			return;
		}

		String subject = "Registration Confirmation";
		String token = UUID.randomUUID().toString();
		String confirmationUrl
			= event.getUrl() + "/pub/regitrationConfirm?activationCode=" + token
			+ "&t=" + System.currentTimeMillis();

		String message = "You registered successfully. To confirm your registration, please click on the below link.";

		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setSubject(subject);
			mailMessage.setTo(email);
			mailMessage.setFrom(mailFrom);
			mailMessage.setText(message + " \r\n" + confirmationUrl);
			if (!mailSendMock) {
				mailSender.send(mailMessage);
				logger.info("Confirmation email sent. {}", authUser.getId() + "-" + authUser.getUsername());
			} else {
				logger.warn("Confirmation email mock sent");
			}

			ThreadUtils.sleep(Duration.ofSeconds(3L));
			logger.warn("This is delay test!");
		} catch (Exception e) {
			logger.error("Mail send error. {}", authUser.getId() + "-" + authUser.getUsername(), e);
		}
	}

	@Inject
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
