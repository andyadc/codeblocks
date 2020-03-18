package com.andyadc.codeblocks.framework.http;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * andy.an
 * 2020/3/18
 */
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	@Override
	public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
		return 0;
	}
}
