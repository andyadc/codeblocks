package com.andyadc.security.extension2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;

import java.io.IOException;

public interface LoginChannelProcessor<T extends AbstractAuthenticationToken> {

	/**
	 * Authentication request authentication.
	 *
	 * @param request the request
	 * @return the authentication
	 * @throws IOException the io exception
	 */
	T authenticationRequest(HttpServletRequest request) throws IOException;

	/**
	 * Gets provider.
	 *
	 * @return the provider
	 */
	AuthenticationProvider getProvider();

	/**
	 * Supports boolean.
	 *
	 * @param channel the channel
	 * @return the boolean
	 */
	boolean supports(String channel);
}
