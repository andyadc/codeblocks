package com.andyadc.security.extension2.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 验证码认证凭据
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -4164653758410464655L;

	private final Object principal;
	private String verificationCode;

	/**
	 * 此构造函数用来初始化未授信凭据.
	 */
	public SmsAuthenticationToken(Object principal, String verificationCode) {
		super(null);
		this.principal = principal;
		this.verificationCode = verificationCode;
		setAuthenticated(false);
	}

	/**
	 * 此构造函数用来初始化授信凭据.
	 */
	public SmsAuthenticationToken(Object principal, String verificationCode,
								  Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.verificationCode = verificationCode;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.verificationCode;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
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
		verificationCode = null;
	}
}
