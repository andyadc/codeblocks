package com.andyadc.bms.security.listener;

import com.andyadc.bms.security.model.UserContext;
import com.andyadc.bms.security.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

	private LoginAttemptService loginAttemptService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		logger.info(">>> AuthenticationSuccessEventListener <<<");
		Authentication authentication = event.getAuthentication();
		UserContext context = (UserContext) authentication.getPrincipal();
		loginAttemptService.loginSucceeded(context.getUsername());
	}

	@Inject
	public void setLoginAttemptService(LoginAttemptService loginAttemptService) {
		this.loginAttemptService = loginAttemptService;
	}
}
