package com.andyadc.test.http.okhttp;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class OkHttpTimeoutLiveTests {

	private static final Logger logger = LoggerFactory.getLogger(OkHttpTimeoutLiveTests.class);

	private static final String HTTP_NON_ROUTABLE_ADDRESS = "http://203.0.113.1";
	private static final String HTTPS_ADDRESS_DELAY_2 = "https://httpbin.org/delay/2";

	@Test
	public void whenConnectTimeoutExceeded_thenSocketTimeoutException() {
		// Given
		OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.MILLISECONDS)
			.build();

		Request request = new Request.Builder()
			.url(HTTP_NON_ROUTABLE_ADDRESS)
			.build();

		// When
		Throwable thrown = Assertions.catchThrowable(() -> client.newCall(request).execute());

		// Then
		Assertions.assertThat(thrown).isInstanceOf(SocketTimeoutException.class);

		logThrown(thrown);
	}

	@Test
	public void whenReadTimeoutExceeded_thenSocketTimeoutException() {
		// Given
		OkHttpClient client = new OkHttpClient.Builder()
			.readTimeout(10, TimeUnit.MILLISECONDS)
			.build();

		Request request = new Request.Builder()
			.url(HTTPS_ADDRESS_DELAY_2)
			.build();

		// When
		Throwable thrown = Assertions.catchThrowable(() -> client.newCall(request).execute());

		// Then
		Assertions.assertThat(thrown).isInstanceOf(SocketTimeoutException.class);

		logThrown(thrown);
	}

	@Test
	public void whenWriteTimeoutExceeded_thenSocketTimeoutException() {
		// Given
		OkHttpClient client = new OkHttpClient.Builder()
			.writeTimeout(10, TimeUnit.MILLISECONDS)
			.build();

		Request request = new Request.Builder()
			.url(HTTPS_ADDRESS_DELAY_2)
			.post(RequestBody.create(create1MBString(), MediaType.parse("text/plain")))
			.build();

		// When
		Throwable thrown = Assertions.catchThrowable(() -> client.newCall(request).execute());

		// Then
		Assertions.assertThat(thrown).isInstanceOf(SocketTimeoutException.class);

		logThrown(thrown);
	}

	@Test
	public void whenCallTimeoutExceeded_thenInterruptedIOException() {
		// Given
		OkHttpClient client = new OkHttpClient.Builder()
			.callTimeout(1, TimeUnit.SECONDS)
			.build();

		Request request = new Request.Builder()
			.url(HTTPS_ADDRESS_DELAY_2)
			.build();

		// When
		Throwable thrown = Assertions.catchThrowable(() -> client.newCall(request).execute());

		// Then
		Assertions.assertThat(thrown).isInstanceOf(InterruptedIOException.class);

		logThrown(thrown);
	}

	@Test
	public void whenPerRequestTimeoutExtended_thenResponseSuccess() throws IOException {
		// Given
		OkHttpClient defaultClient = new OkHttpClient.Builder()
			.readTimeout(1, TimeUnit.SECONDS)
			.build();

		Request request = new Request.Builder()
			.url(HTTPS_ADDRESS_DELAY_2)
			.build();

		Throwable thrown = Assertions.catchThrowable(() -> defaultClient.newCall(request).execute());

		Assertions.assertThat(thrown).isInstanceOf(InterruptedIOException.class);

		// When
		OkHttpClient extendedTimeoutClient = defaultClient.newBuilder()
			.readTimeout(5, TimeUnit.SECONDS)
			.build();

		// Then
		Response response = extendedTimeoutClient.newCall(request).execute();
		Assertions.assertThat(response.code()).isEqualTo(200);
	}

	private void logThrown(Throwable thrown) {
		logger.info("Thrown: ", thrown);
	}

	private String create1MBString() {
		return new String(new char[512 * 1024]);
	}
}
