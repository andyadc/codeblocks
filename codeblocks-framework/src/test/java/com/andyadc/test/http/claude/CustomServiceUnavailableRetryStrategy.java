package com.andyadc.test.http.claude;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retry strategy for handling 503 Service Unavailable responses
 */
public class CustomServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {

	private static final Logger logger = LoggerFactory.getLogger(CustomServiceUnavailableRetryStrategy.class);

	private static final int MAX_RETRIES = 3;
	private static final long RETRY_INTERVAL = 2000; // 2 seconds

	@Override
	public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
		int statusCode = response.getStatusLine().getStatusCode();

		// Retry on 503 Service Unavailable or 429 Too Many Requests
		boolean shouldRetry = executionCount <= MAX_RETRIES &&
			(statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE ||
				statusCode == 429);

		if (shouldRetry) {
			logger.warn("Received status {} (attempt {}/{}), will retry",
				statusCode, executionCount, MAX_RETRIES);

			// Consume entity to release connection
			try {
				EntityUtils.consume(response.getEntity());
			} catch (Exception e) {
				logger.error("Error consuming entity", e);
			}
		}

		return shouldRetry;
	}

	@Override
	public long getRetryInterval() {
		return RETRY_INTERVAL;
	}

}
