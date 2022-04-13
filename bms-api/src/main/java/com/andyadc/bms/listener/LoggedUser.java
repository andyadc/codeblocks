package com.andyadc.bms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

//TODO
@Component
public class LoggedUser implements HttpSessionBindingListener {

	private static final Logger logger = LoggerFactory.getLogger(LoggedUser.class);

	private String username;

	public LoggedUser() {
	}

	public LoggedUser(String username) {
		this.username = username;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		logger.info("valueBound >>> {}-{}", username, event);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		logger.info("valueUnbound >>> {}-{}", username, event);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
