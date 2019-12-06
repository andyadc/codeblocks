package com.andyadc.codeblocks.framework.http;

import java.util.Map;

/**
 * andy.an
 * 2019/12/6
 */
public interface HttpClientTemplate extends Lifecycle {

	String get(String uri);

	String get(String uri, Map<String, String> parameters);

	String get(String uri, Map<String, String> parameters, Map<String, String> headers);

	String post(String uri);

	String post(String uri, String content);

	String post(String uri, String content, Map<String, String> parameters);

	String post(String uri, String content, Map<String, String> parameters, Map<String, String> headers);

	Object getClient();

	String clientType();
}
