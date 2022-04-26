package com.andyadc.bms.security.extension.jwt;

import org.springframework.security.oauth2.jwt.JwtException;

public class JwtEncodingException extends JwtException {

	private static final long serialVersionUID = 5580237937471711931L;

	/**
	 * Constructs a {@code JwtEncodingException} using the provided parameters.
	 *
	 * @param message the detail message
	 */
	public JwtEncodingException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@code JwtEncodingException} using the provided parameters.
	 *
	 * @param message the detail message
	 * @param cause   the root cause
	 */
	public JwtEncodingException(String message, Throwable cause) {
		super(message, cause);
	}
}
