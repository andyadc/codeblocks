package com.andyadc.codeblocks.framework.http;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles asynchronous HTTP requests
 */
public class AsyncOkHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(AsyncOkHttpClient.class);

	private final OkHttpClient client;
	private final ExecutorService executorService;

	public AsyncOkHttpClient(OkHttpClient client) {
		this.client = client;
		this.executorService = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() * 2
		);
	}

	/**
	 * Execute request asynchronously using CompletableFuture
	 */
	public <T> CompletableFuture<T> executeAsync(
		Request request,
		ResponseParser<T> parser) {

		CompletableFuture<T> future = new CompletableFuture<>();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				logger.error("Async request failed: {}", request.url(), e);
				future.completeExceptionally(e);
			}

			@Override
			public void onResponse(Call call, Response response) {
				try (response) {
					if (!response.isSuccessful()) {
						future.completeExceptionally(
							new HttpException("HTTP " + response.code(),
								response.code())
						);
						return;
					}

					ResponseBody body = response.body();
					if (body == null) {
						future.completeExceptionally(
							new IllegalStateException("Empty response body")
						);
						return;
					}

					T result = parser.parse(body.string());
					future.complete(result);

				} catch (Exception e) {
					logger.error("Failed to parse response: {}", request.url(), e);
					future.completeExceptionally(e);
				}
			}
		});

		return future;
	}

	/**
	 * Execute multiple requests in parallel
	 */
	public <T> CompletableFuture<java.util.List<T>> executeAllAsync(
		java.util.List<Request> requests,
		ResponseParser<T> parser) {

		java.util.List<CompletableFuture<T>> futures = requests.stream()
			.map(request -> executeAsync(request, parser))
			.collect(java.util.stream.Collectors.toList());

		return CompletableFuture.allOf(
			futures.toArray(new CompletableFuture[0])
		).thenApply(v -> futures.stream()
			.map(CompletableFuture::join)
			.collect(java.util.stream.Collectors.toList())
		);
	}

	public void shutdown() {
		executorService.shutdown();
	}

	@FunctionalInterface
	public interface ResponseParser<T> {
		T parse(String body) throws Exception;
	}

	public static class HttpException extends Exception {
		private final int statusCode;

		public HttpException(String message, int statusCode) {
			super(message);
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}

}
