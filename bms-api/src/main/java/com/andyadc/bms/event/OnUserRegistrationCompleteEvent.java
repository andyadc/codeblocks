package com.andyadc.bms.event;

import com.andyadc.bms.auth.entity.AuthUser;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 3326046890141966913L;

	private AuthUser authUser;
	private Locale locale;
	private String url;

	public OnUserRegistrationCompleteEvent(AuthUser authUser) {
		super(authUser);
		this.authUser = authUser;
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
