package com.andyadc.bms.security.configurers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bms.security.jwt")
public class JwtSettings {

	/**
	 * will expire after this time.
	 */
	private Integer tokenExpirationTime;
	/**
	 * can be refreshed during this timeframe.
	 */
	private Integer refreshTokenExpTime;
	/**
	 * Token issuer.
	 */
	private String tokenIssuer;
	/**
	 * Key is used to sign .
	 */
	private String tokenSigningKey;

	public Integer getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(Integer tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

	public Integer getRefreshTokenExpTime() {
		return refreshTokenExpTime;
	}

	public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
		this.refreshTokenExpTime = refreshTokenExpTime;
	}

	public String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public String getTokenSigningKey() {
		return tokenSigningKey;
	}

	public void setTokenSigningKey(String tokenSigningKey) {
		this.tokenSigningKey = tokenSigningKey;
	}
}
