package com.andyadc.codeblocks.framework.http;

import java.util.Map;

/**
 * andy.an
 * 2019/12/6
 */
public abstract class AbstractHttpClientTemplate implements HttpClientTemplate {

	protected static final String CONTENT_TYPE_JSON = "application/json"; // default
	// json
	protected static final String CONTENT_TYPE_JSON_PATTERN = CONTENT_TYPE_JSON + "; charset={0}";// default
	protected Map<String, String> globalHeaders;
	protected HttpClientConfiguration configuration = HttpClientConfiguration.common();

	protected String charset = configuration.getCharset();

	private volatile boolean init = false;

	public AbstractHttpClientTemplate() {
	}

	public AbstractHttpClientTemplate(HttpClientConfiguration configuration) {
		if (configuration != null) {
			this.configuration = configuration;
			this.globalHeaders = configuration.getGlobalHeaders();
		}
	}

	public void setGlobalHeaders(Map<String, String> globalHeaders) {
		this.globalHeaders = globalHeaders;
		configuration.setGlobalHeaders(globalHeaders);
	}

	public synchronized void init() {
		if (init) {
			return;
		}
		init = true;
	}

	protected HttpClientConfiguration configuration() {
		return this.configuration;
	}
}
