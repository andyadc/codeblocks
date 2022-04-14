package com.andyadc.bms.security;

import org.springframework.context.MessageSource;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SecAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private MessageSource messages;

	@Inject
	public void setMessages(MessageSource messages) {
		this.messages = messages;
	}
}
