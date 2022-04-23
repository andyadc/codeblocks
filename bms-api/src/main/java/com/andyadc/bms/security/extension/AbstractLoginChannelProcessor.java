package com.andyadc.bms.security.extension;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class AbstractLoginChannelProcessor<T extends AbstractAuthenticationToken>
	implements LoginChannelProcessor<T> {

	private final String channel;
	private final AuthenticationProvider authenticationProvider;

	/**
	 * The Authentication details source.
	 */
	protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	public AbstractLoginChannelProcessor(String channel, AuthenticationProvider authenticationProvider) {
		this.channel = channel;
		this.authenticationProvider = authenticationProvider;
	}

	@Override
	public T authenticationRequest(HttpServletRequest request) throws IOException {
		T authenticationRequest = doAuthenticationRequest(request);
		setDetails(request, authenticationRequest);
		return authenticationRequest;
	}

	/**
	 * Do authentication request t.
	 */
	public abstract T doAuthenticationRequest(HttpServletRequest request) throws IOException;

	@Override
	public AuthenticationProvider getProvider() {
		return this.authenticationProvider;
	}

	@Override
	public boolean supports(String channel) {
		return this.channel.equals(channel);
	}

	/**
	 * Set details.
	 */
	protected void setDetails(HttpServletRequest request, T authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
}
