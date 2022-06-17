package com.andyadc.bms.security.handler;

import com.andyadc.bms.security.model.SecureUser;
import com.andyadc.bms.security.model.TokenResponse;

public interface TokenStorage {

	/**
	 * Expire.
	 *
	 * @param user the SecureUser
	 */
	void expire(SecureUser user);

	/**
	 * Get jwt token pair.
	 *
	 * @param user the SecureUser
	 * @return the jwt token pair
	 */
	TokenResponse get(SecureUser user);
}
