package com.andyadc.security.extension2.jwt;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BearerTokenResponse implements Serializable {

	private static final long serialVersionUID = 4089977300536355901L;

	private AccessToken accessToken;

	private RefreshToken refreshToken;

	private Map<String, Object> additionalParameters;

	/**
	 * From token response.
	 *
	 * @param oAuth2AccessTokenResponse the o auth 2 access token response
	 * @return the token response
	 */
	public static BearerTokenResponse from(OAuth2AccessTokenResponse oAuth2AccessTokenResponse) {
		BearerTokenResponse bearerTokenResponse = new BearerTokenResponse();
		OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenResponse.getAccessToken();
		AccessToken accessToken = new AccessToken();
		accessToken.setTokenValue(oAuth2AccessToken.getTokenValue());
		accessToken.setIssuedAt(oAuth2AccessToken.getIssuedAt());
		accessToken.setExpiresAt(oAuth2AccessToken.getExpiresAt());
		accessToken.setScopes(oAuth2AccessToken.getScopes());
		bearerTokenResponse.setAccessToken(accessToken);

		OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessTokenResponse.getRefreshToken();

		RefreshToken refreshToken = new RefreshToken();

		if (Objects.nonNull(oAuth2RefreshToken)) {
			refreshToken.setTokenValue(oAuth2RefreshToken.getTokenValue());
			refreshToken.setIssuedAt(oAuth2RefreshToken.getIssuedAt());
			refreshToken.setExpiresAt(oAuth2RefreshToken.getExpiresAt());
		}
		bearerTokenResponse.setRefreshToken(refreshToken);
		bearerTokenResponse.setAdditionalParameters(oAuth2AccessTokenResponse.getAdditionalParameters());
		return bearerTokenResponse;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Map<String, Object> getAdditionalParameters() {
		return additionalParameters;
	}

	public void setAdditionalParameters(Map<String, Object> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}

	public static class AccessToken {
		private String tokenValue;

		private Instant issuedAt;

		private Instant expiresAt;

		private Set<String> scopes;

		public String getTokenValue() {
			return tokenValue;
		}

		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}

		public Instant getIssuedAt() {
			return issuedAt;
		}

		public void setIssuedAt(Instant issuedAt) {
			this.issuedAt = issuedAt;
		}

		public Instant getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Instant expiresAt) {
			this.expiresAt = expiresAt;
		}

		public Set<String> getScopes() {
			return scopes;
		}

		public void setScopes(Set<String> scopes) {
			this.scopes = scopes;
		}
	}

	public static class RefreshToken {

		private String tokenValue;

		private Instant issuedAt;

		private Instant expiresAt;

		public String getTokenValue() {
			return tokenValue;
		}

		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}

		public Instant getIssuedAt() {
			return issuedAt;
		}

		public void setIssuedAt(Instant issuedAt) {
			this.issuedAt = issuedAt;
		}

		public Instant getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Instant expiresAt) {
			this.expiresAt = expiresAt;
		}
	}
}
