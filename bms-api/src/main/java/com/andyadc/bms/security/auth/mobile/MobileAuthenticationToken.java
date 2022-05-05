package com.andyadc.bms.security.auth.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MobileAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 4550004532000330425L;

	private final Object principal;
	private String verificationCode;

	public MobileAuthenticationToken(Object principal, String verificationCode) {
		super(null);
		this.principal = principal;
		this.verificationCode = verificationCode;
		this.setAuthenticated(false);
	}

	public MobileAuthenticationToken(Object principal, String verificationCode, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.verificationCode = verificationCode;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return verificationCode;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.verificationCode = null;
	}
}
