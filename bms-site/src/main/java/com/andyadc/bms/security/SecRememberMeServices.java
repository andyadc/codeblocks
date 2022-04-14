package com.andyadc.bms.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class SecRememberMeServices extends PersistentTokenBasedRememberMeServices {

	public SecRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
	}
}
