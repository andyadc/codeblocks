package com.andyadc.codeblocks.framework.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.util.Arrays;

/**
 * 自定义 KeepAlive 策略
 */
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	/**
	 * 如果没有约定，则默认定义时长为 30s
	 */
	private static final long DEFAULT_KEEP_ALIVE_SECONDS = 30L;

	public CustomConnectionKeepAliveStrategy() {
	}

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		return Arrays.stream(response.getHeaders(HTTP.CONN_KEEP_ALIVE))
			.filter(
				h -> StringUtils.equalsIgnoreCase(h.getName(), "timeout")
					&& StringUtils.isNumeric(h.getValue())
			)
			.findFirst()
			.map(h -> NumberUtils.toLong(h.getValue(), DEFAULT_KEEP_ALIVE_SECONDS))
			.orElse(DEFAULT_KEEP_ALIVE_SECONDS) * 1000L;
	}
}
