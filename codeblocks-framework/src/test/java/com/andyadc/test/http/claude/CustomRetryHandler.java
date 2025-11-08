package com.andyadc.test.http.claude;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom retry handler with configurable retry logic
 */
public class CustomRetryHandler implements HttpRequestRetryHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomRetryHandler.class);

	private static final int MAX_RETRIES = 3;

	private static final Set<Class<? extends IOException>> NON_RETRIABLE_EXCEPTIONS =
		new HashSet<>(Arrays.asList(
			InterruptedIOException.class,
			UnknownHostException.class,
			ConnectTimeoutException.class,
			SSLException.class
		));

	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		logger.warn("Request failed (attempt {}/{}): {}",
			executionCount, MAX_RETRIES, exception.getMessage());

		if (executionCount > MAX_RETRIES) {
			logger.error("Max retry attempts ({}) exceeded", MAX_RETRIES);
			return false;
		}

		// Don't retry on non-retriable exceptions
		for (Class<? extends IOException> exceptionClass : NON_RETRIABLE_EXCEPTIONS) {
			if (exceptionClass.isInstance(exception)) {
				logger.warn("Non-retriable exception: {}", exception.getClass().getName());
				return false;
			}
		}

		// Don't retry if request has already been sent (for idempotency)
		HttpClientContext clientContext = HttpClientContext.adapt(context);
		HttpRequest request = clientContext.getRequest();

		// Retry only idempotent requests
		boolean isIdempotent = !(request instanceof HttpEntityEnclosingRequest);

		if (!isIdempotent) {
			logger.warn("Request is not idempotent, not retrying");
			return false;
		}

		// Add exponential backoff
		try {
			long backoffTime = (long) Math.pow(2, executionCount) * 1000L;
			logger.info("Backing off for {} ms before retry", backoffTime);
			Thread.sleep(backoffTime);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}

		return true;
	}
}
