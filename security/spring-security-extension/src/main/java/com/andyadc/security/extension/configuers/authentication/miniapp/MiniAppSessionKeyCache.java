package com.andyadc.security.extension.configuers.authentication.miniapp;

public interface MiniAppSessionKeyCache {

	/**
	 * Put sessionKey.
	 *
	 * @param cacheKey   {@code clientId::openId}
	 * @param sessionKey the session key
	 * @return sessionKey
	 */
	String put(String cacheKey, String sessionKey);

	/**
	 * Get sessionKey.
	 *
	 * @param cacheKey {@code clientId::openId}
	 * @return sessionKey
	 */
	String get(String cacheKey);
}
