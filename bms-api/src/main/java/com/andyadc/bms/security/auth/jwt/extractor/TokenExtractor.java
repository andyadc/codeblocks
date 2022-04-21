package com.andyadc.bms.security.auth.jwt.extractor;

/**
 * Implementations of this interface should always return raw base-64 encoded
 * representation of JWT Token.
 */
public interface TokenExtractor {

	/**
	 * @param header http request header
	 * @return token
	 */
	String extract(String header);
}
