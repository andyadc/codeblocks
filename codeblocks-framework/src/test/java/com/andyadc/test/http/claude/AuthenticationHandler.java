package com.andyadc.test.http.claude;

import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AuthenticationHandler {

	/**
	 * Basic Authentication
	 */
	public static CloseableHttpClient createBasicAuthClient(
		String username, String password, String host, int port) {

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(
			new AuthScope(host, port),
			new UsernamePasswordCredentials(username, password)
		);

		return HttpClients.custom()
			.setDefaultCredentialsProvider(credentialsProvider)
			.build();
	}

	/**
	 * Preemptive Basic Authentication
	 */
	public static CloseableHttpClient createPreemptiveBasicAuthClient(
		String username, String password) {

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(
			AuthScope.ANY,
			new UsernamePasswordCredentials(username, password)
		);

		return HttpClients.custom()
			.setDefaultCredentialsProvider(credentialsProvider)
			.build();
	}

	/**
	 * Bearer Token Authentication
	 */
	public static class BearerTokenAuth {
		private final String token;

		public BearerTokenAuth(String token) {
			this.token = token;
		}

		public void applyToRequest(HttpRequestBase request) {
			request.setHeader("Authorization", "Bearer " + token);
		}
	}

	/**
	 * API Key Authentication
	 */
	public static class ApiKeyAuth {
		private final String apiKey;
		private final String headerName;

		public ApiKeyAuth(String apiKey, String headerName) {
			this.apiKey = apiKey;
			this.headerName = headerName;
		}

		public void applyToRequest(HttpRequestBase request) {
			request.setHeader(headerName, apiKey);
		}
	}

	/**
	 * OAuth 2.0 Support
	 */
	public static class OAuth2Handler {
		private volatile String accessToken;
		private volatile long expiresAt;

		public OAuth2Handler(String initialToken, long expiresIn) {
			this.accessToken = initialToken;
			this.expiresAt = System.currentTimeMillis() + (expiresIn * 1000);
		}

		public synchronized void refreshToken(String newToken, long expiresIn) {
			this.accessToken = newToken;
			this.expiresAt = System.currentTimeMillis() + (expiresIn * 1000);
		}

		public String getAccessToken() {
			if (System.currentTimeMillis() >= expiresAt) {
				throw new RuntimeException("Access token expired");
			}
			return accessToken;
		}

		public boolean isTokenExpired() {
			return System.currentTimeMillis() >= expiresAt;
		}

		public void applyToRequest(HttpRequestBase request) {
			request.setHeader("Authorization", "Bearer " + getAccessToken());
		}
	}

	/**
	 * NTLM Authentication (Windows) TODO
	 */

}
