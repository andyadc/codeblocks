package com.andyadc.bms.security.extension.jwt;

import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public interface JwtTokenStorage {

	/**
	 * Put jwt token pair.
	 *
	 * @param tokenResponse the token response
	 * @param userId        the user id
	 * @return the jwt token pair
	 */
	BearerTokenResponse put(OAuth2AccessTokenResponse tokenResponse, String userId);

	/**
	 * Expire.
	 *
	 * @param userId the user id
	 */
	void expire(String userId);

	/**
	 * Get jwt token pair.
	 *
	 * @param userId the user id
	 * @return the jwt token pair
	 */
	BearerTokenResponse get(String userId);
}
