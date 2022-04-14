package com.andyadc.bms.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SecAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return super.authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return super.supports(authentication);
	}
}
