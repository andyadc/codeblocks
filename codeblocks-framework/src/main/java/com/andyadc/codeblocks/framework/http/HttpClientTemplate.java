package com.andyadc.codeblocks.framework.http;

import java.util.Map;

public interface HttpClientTemplate extends Lifecycle {

	String get(String url);

	String get(String url, Map<String, String> parameters);

	String get(String url, Map<String, String> parameters, Map<String, String> headers);

	String post(String url);

	String post(String url, String content);

	String post(String url, String content, Map<String, String> parameters);

	String post(String url, String content, Map<String, String> parameters, Map<String, String> headers);

	String form(String url, Map<String, String> parameters);

	String form(String url, Map<String, String> parameters, Map<String, String> headers);

	Object getClient();

	String clientType();
}
