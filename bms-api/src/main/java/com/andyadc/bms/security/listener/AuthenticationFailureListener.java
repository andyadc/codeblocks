package com.andyadc.bms.security.listener;

import com.andyadc.bms.security.service.LoginAttemptService;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureListener.class);

	private LoginAttemptService loginAttemptService;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		logger.info(">>> AuthenticationFailureListener <<<");
		Authentication authentication = event.getAuthentication();
		loginAttemptService.loginFailed((String) authentication.getPrincipal());
	}

	@Inject
	public void setLoginAttemptService(LoginAttemptService loginAttemptService) {
		this.loginAttemptService = loginAttemptService;
	}
}
