package com.andyadc.bms.event.listener;

import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.service.EmailService;
import com.andyadc.codeblocks.kit.text.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Order(1)
@Component
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationCompleteListener.class);

	private EmailService mailService;

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
		String userMailLog = authUser.getId() + "-" + authUser.getUsername();
		logger.info("Send mail after user registration. {}", userMailLog);

		String email = authUser.getEmail();
		if (StringUtil.isBlank(email)) {
			logger.warn("User email is null.");
			return;
		}

		String token = UUID.randomUUID().toString();
		String confirmationUrl = event.getUrl() + "/pub/regitrationConfirm?activationCode=" + token
			+ "&t=" + System.currentTimeMillis();

		String subject = "Registration Confirmation";
		String message = "You registered successfully. To confirm your registration, please click on the below link.";
		String text = message + " \r\n" + confirmationUrl;

		if (mailSendMock) {
			logger.warn("Confirmation email mock sent. {}", userMailLog);
		} else {
			boolean sendFlag = mailService.sendSimpleMessage(email, subject, text);
			if (sendFlag) {
				logger.info("Confirmation email sent. {}", userMailLog);
			} else {
				logger.warn("Confirmation email send fail. {}", userMailLog);
			}
		}
	}

	@Inject
	public void setMailService(EmailService mailService) {
		this.mailService = mailService;
	}
}
