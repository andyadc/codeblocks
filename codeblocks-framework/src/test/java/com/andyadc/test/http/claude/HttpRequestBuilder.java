package com.andyadc.test.http.claude;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpEntity;

public class HttpRequestBuilder {

	/**
	 * Creates production-ready request configuration
	 */
	public static RequestConfig createProductionRequestConfig() {
		return RequestConfig.custom()
			.setConnectTimeout(5000) // Connection timeout: 5 seconds
			.setSocketTimeout(10000) // Socket timeout: 10 seconds
			.setConnectionRequestTimeout(3000) // Pool timeout: 3 seconds
			.setRedirectsEnabled(true)
			.setMaxRedirects(3)
			.setCircularRedirectsAllowed(false)
			.setExpectContinueEnabled(false)
			.build();
	}

	/**
	 * Builder for GET requests
	 */
	public static class GetRequestBuilder {
		private final HttpGet request;
		private RequestConfig config;

		public GetRequestBuilder(String url) {
			this.request = new HttpGet(url);
			this.config = createProductionRequestConfig();
		}

		public GetRequestBuilder addHeader(String name, String value) {
			request.addHeader(name, value);
			return this;
		}

		public GetRequestBuilder withConfig(RequestConfig config) {
			this.config = config;
			return this;
		}

		public GetRequestBuilder withTimeout(int timeoutMs) {
			this.config = RequestConfig.copy(config)
				.setSocketTimeout(timeoutMs)
				.build();
			return this;
		}

		public HttpGet build() {
			request.setConfig(config);
			return request;
		}
	}

	/**
	 * Builder for POST requests
	 */
	public static class PostRequestBuilder {
		private final HttpPost request;
		private RequestConfig config;

		public PostRequestBuilder(String url) {
			this.request = new HttpPost(url);
			this.config = createProductionRequestConfig();
		}

		public PostRequestBuilder addHeader(String name, String value) {
			request.addHeader(name, value);
			return this;
		}

		public PostRequestBuilder withJsonBody(String json) {
			StringEntity entity = new StringEntity(
				json, ContentType.APPLICATION_JSON);
			request.setEntity(entity);
			return this;
		}

		public PostRequestBuilder withBody(HttpEntity entity) {
			request.setEntity(entity);
			return this;
		}

		public PostRequestBuilder withConfig(RequestConfig config) {
			this.config = config;
			return this;
		}

		public HttpPost build() {
			request.setConfig(config);
			return request;
		}
	}

	/**
	 * Builder for PUT requests
	 */
	public static class PutRequestBuilder {
		private final HttpPut request;
		private RequestConfig config;

		public PutRequestBuilder(String url) {
			this.request = new HttpPut(url);
			this.config = createProductionRequestConfig();
		}

		public PutRequestBuilder addHeader(String name, String value) {
			request.addHeader(name, value);
			return this;
		}

		public PutRequestBuilder withJsonBody(String json) {
			StringEntity entity = new StringEntity(
				json, ContentType.APPLICATION_JSON);
			request.setEntity(entity);
			return this;
		}

		public HttpPut build() {
			request.setConfig(config);
			return request;
		}
	}

	/**
	 * Builder for DELETE requests
	 */
	public static class DeleteRequestBuilder {
		private final HttpDelete request;
		private RequestConfig config;

		public DeleteRequestBuilder(String url) {
			this.request = new HttpDelete(url);
			this.config = createProductionRequestConfig();
		}

		public DeleteRequestBuilder addHeader(String name, String value) {
			request.addHeader(name, value);
			return this;
		}

		public HttpDelete build() {
			request.setConfig(config);
			return request;
		}
	}

}
