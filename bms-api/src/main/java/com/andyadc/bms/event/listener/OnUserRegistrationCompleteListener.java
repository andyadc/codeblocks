package com.andyadc.bms.event.listener;

import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	//TODO
	@Async
	@Override
	public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnUserRegistrationCompleteEvent event) {
		try {
			AuthUser authUser = event.getAuthUser();
			logger.info("AuthUser [{}-{}] register completed", authUser.getId(), authUser.getUsername());

			String subject = "Registration Confirmation";
			String token = UUID.randomUUID().toString();
			String confirmationUrl
				= event.getUrl() + "/regitrationConfirm.html?token=" + token;

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setSubject(subject);
			mailMessage.setTo(authUser.getEmail());

			logger.info("Mail {}", mailMessage);
//			mailSender.send(mailMessage);
			ThreadUtils.sleep(Duration.ofSeconds(3L));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Inject
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
