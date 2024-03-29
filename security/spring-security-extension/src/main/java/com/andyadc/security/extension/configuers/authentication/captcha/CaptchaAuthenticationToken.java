package com.andyadc.security.extension.configuers.authentication.captcha;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8975729746026281785L;

	private final Object principal;
	private String captcha;

	/**
	 * 此构造函数用来初始化未授信凭据.
	 *
	 * @param principal the principal
	 * @param captcha   the captcha
	 */
	public CaptchaAuthenticationToken(Object principal, String captcha) {
		super(null);
		this.principal = principal;
		this.captcha = captcha;
		setAuthenticated(false);
	}

	/**
	 * 此构造函数用来初始化授信凭据.
	 *
	 * @param principal   the principal
	 * @param captcha     the captcha
	 * @param authorities the authorities
	 */
	public CaptchaAuthenticationToken(Object principal, String captcha,
									  Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.captcha = captcha;
		// must use super, as we override
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.captcha;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		captcha = null;
	}
}
