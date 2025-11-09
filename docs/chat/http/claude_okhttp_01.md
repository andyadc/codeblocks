# Comprehensive OkHttp Production Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Basic Setup](#basic-setup)
3. [Connection Pooling](#connection-pooling)
4. [Timeouts Configuration](#timeouts-configuration)
5. [Interceptors](#interceptors)
6. [Error Handling](#error-handling)
7. [Retry & Circuit Breaking](#retry-circuit-breaking)
8. [Authentication](#authentication)
9. [Monitoring & Logging](#monitoring-logging)
10. [Production Best Practices](#production-best-practices)

## Introduction

OkHttp is a production-ready HTTP client for Java and Android applications. This guide covers essential patterns for production environments.

## Basic Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.12.0</version>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>logging-interceptor</artifactId>
        <version>4.12.0</version>
    </dependency>
    <!-- Optional: For Mockito testing -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>mockwebserver</artifactId>
        <version>4.12.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Dependencies

```gradle
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.12.0'
}
```

## Production-Ready OkHttp Client

### Core Configuration Class

```java
package com.example.http;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Production-ready OkHttp client configuration
 */
public class OkHttpClientFactory {

    private static final int MAX_IDLE_CONNECTIONS = 50;
    private static final int KEEP_ALIVE_DURATION_MS = 300000; // 5 minutes
    private static final int CONNECTION_TIMEOUT_MS = 10000;   // 10 seconds
    private static final int READ_TIMEOUT_MS = 30000;         // 30 seconds
    private static final int WRITE_TIMEOUT_MS = 30000;        // 30 seconds

    private final String baseUrl;
    private final boolean enableLogging;
    private final boolean enableRetry;

    public OkHttpClientFactory(String baseUrl, boolean enableLogging, boolean enableRetry) {
        this.baseUrl = baseUrl;
        this.enableLogging = enableLogging;
        this.enableRetry = enableRetry;
    }

    /**
     * Creates a production-ready OkHttpClient instance
     */
    public OkHttpClient createClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            // Connection pool configuration
            .connectionPool(new ConnectionPool(
                MAX_IDLE_CONNECTIONS,
                KEEP_ALIVE_DURATION_MS,
                TimeUnit.MILLISECONDS
            ))

            // Timeout configuration
            .connectTimeout(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)

            // Retry on connection failure
            .retryOnConnectionFailure(enableRetry)

            // Follow redirects
            .followRedirects(true)
            .followSslRedirects(true);

        // Add interceptors
        addInterceptors(builder);

        return builder.build();
    }

    private void addInterceptors(OkHttpClient.Builder builder) {
        // Logging interceptor
        if (enableLogging) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // Custom interceptors
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new MetricsInterceptor());
        builder.addInterceptor(new AuthInterceptor());
        builder.addNetworkInterceptor(new CompressionInterceptor());
    }
}
```

## Connection Pooling

### Advanced Connection Pool Configuration

```java
package com.example.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolConfig {

    /**
     * Creates a client with optimized connection pooling
     * for high-traffic scenarios
     */
    public static OkHttpClient createHighThroughputClient() {
        ConnectionPool connectionPool = new ConnectionPool(
            100,                    // Max idle connections
            5,                      // Keep alive duration
            TimeUnit.MINUTES
        );

        return new OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .build();
    }

    /**
     * Creates a client with minimal connection pooling
     * for low-traffic scenarios
     */
    public static OkHttpClient createLowThroughputClient() {
        ConnectionPool connectionPool = new ConnectionPool(
            5,                      // Max idle connections
            1,                      // Keep alive duration
            TimeUnit.MINUTES
        );

        return new OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .build();
    }
}
```

## Timeouts Configuration

### Dynamic Timeout Configuration

```java
package com.example.http;

import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TimeoutConfiguration {

    /**
     * Client with different timeout configurations
     */
    public static class TimeoutConfig {
        private final int connectTimeout;
        private final int readTimeout;
        private final int writeTimeout;
        private final int callTimeout;

        public TimeoutConfig(int connectTimeout, int readTimeout,
                           int writeTimeout, int callTimeout) {
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            this.writeTimeout = writeTimeout;
            this.callTimeout = callTimeout;
        }

        public OkHttpClient createClient() {
            return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .callTimeout(callTimeout, TimeUnit.SECONDS)
                .build();
        }
    }

    /**
     * Per-request timeout override
     */
    public static Response executeWithCustomTimeout(
            OkHttpClient client,
            Request request,
            int timeoutSeconds) throws IOException {

        OkHttpClient customClient = client.newBuilder()
            .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .build();

        try (Response response = customClient.newCall(request).execute()) {
            return response;
        }
    }

    /**
     * Preset configurations for different scenarios
     */
    public static final TimeoutConfig QUICK_REQUEST =
        new TimeoutConfig(5, 10, 10, 15);

    public static final TimeoutConfig STANDARD_REQUEST =
        new TimeoutConfig(10, 30, 30, 45);

    public static final TimeoutConfig LONG_RUNNING_REQUEST =
        new TimeoutConfig(10, 120, 120, 180);
}
```

## Interceptors

### Header Interceptor

```java
package com.example.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.UUID;

/**
 * Adds common headers to all requests
 */
public class HeaderInterceptor implements Interceptor {

    private static final String USER_AGENT = "MyApp/1.0";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
            .header("User-Agent", USER_AGENT)
            .header("Accept", "application/json")
            .header("Accept-Encoding", "gzip, deflate")
            .header(CORRELATION_ID_HEADER, generateCorrelationId())
            .method(original.method(), original.body());

        return chain.proceed(requestBuilder.build());
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
```

### Authentication Interceptor

```java
package com.example.http.interceptors;

import okhttp3.*;
import java.io.IOException;

/**
 * Handles authentication for requests
 */
public class AuthInterceptor implements Interceptor {

    private final TokenProvider tokenProvider;

    public AuthInterceptor() {
        this.tokenProvider = new TokenProvider();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Skip auth for certain endpoints
        if (shouldSkipAuth(original)) {
            return chain.proceed(original);
        }

        String token = tokenProvider.getToken();

        Request authenticated = original.newBuilder()
            .header("Authorization", "Bearer " + token)
            .build();

        Response response = chain.proceed(authenticated);

        // Handle token refresh on 401
        if (response.code() == 401) {
            response.close();

            synchronized (this) {
                String newToken = tokenProvider.refreshToken();
                Request retryRequest = original.newBuilder()
                    .header("Authorization", "Bearer " + newToken)
                    .build();

                return chain.proceed(retryRequest);
            }
        }

        return response;
    }

    private boolean shouldSkipAuth(Request request) {
        String path = request.url().encodedPath();
        return path.contains("/login") || path.contains("/public");
    }

    /**
     * Token provider implementation
     */
    private static class TokenProvider {
        private String currentToken = "initial-token";

        public String getToken() {
            return currentToken;
        }

        public String refreshToken() {
            // Implement actual token refresh logic
            currentToken = "refreshed-token-" + System.currentTimeMillis();
            return currentToken;
        }
    }
}
```

### Metrics Interceptor

```java
package com.example.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Collects metrics for HTTP requests
 */
public class MetricsInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MetricsInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();

        String method = request.method();
        String url = request.url().toString();

        Response response = null;
        try {
            response = chain.proceed(request);

            long duration = System.currentTimeMillis() - startTime;
            int statusCode = response.code();

            // Log metrics
            logMetrics(method, url, statusCode, duration, null);

            // Send to monitoring system (e.g., Prometheus, DataDog)
            recordMetrics(method, url, statusCode, duration);

            return response;

        } catch (IOException e) {
            long duration = System.currentTimeMillis() - startTime;
            logMetrics(method, url, -1, duration, e);
            throw e;
        }
    }

    private void logMetrics(String method, String url, int statusCode,
                           long duration, Exception error) {
        if (error != null) {
            logger.error("Request failed: {} {} - Duration: {}ms - Error: {}",
                method, url, duration, error.getMessage());
        } else {
            logger.info("Request completed: {} {} - Status: {} - Duration: {}ms",
                method, url, statusCode, duration);
        }
    }

    private void recordMetrics(String method, String url, int statusCode, long duration) {
        // Integration with metrics library
        // Example: Micrometer, Dropwizard Metrics, etc.
        // MetricsRegistry.timer("http.request.duration")
        //     .tag("method", method)
        //     .tag("status", String.valueOf(statusCode))
        //     .record(duration, TimeUnit.MILLISECONDS);
    }
}
```

### Compression Interceptor

```java
package com.example.http.interceptors;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import java.io.IOException;

/**
 * Compresses request bodies
 */
public class CompressionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.body() == null ||
            originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }

        Request compressedRequest = originalRequest.newBuilder()
            .header("Content-Encoding", "gzip")
            .method(originalRequest.method(), gzip(originalRequest.body()))
            .build();

        return chain.proceed(compressedRequest);
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // Unknown
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
```

## Error Handling

### Robust Error Handling Wrapper

```java
package com.example.http;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Comprehensive error handling for HTTP requests
 */
public class HttpRequestExecutor {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestExecutor.class);
    private final OkHttpClient client;

    public HttpRequestExecutor(OkHttpClient client) {
        this.client = client;
    }

    /**
     * Execute request with comprehensive error handling
     */
    public <T> ApiResponse<T> execute(Request request, ResponseParser<T> parser) {
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                return handleErrorResponse(response);
            }

            ResponseBody body = response.body();
            if (body == null) {
                return ApiResponse.error("Empty response body",
                    response.code(), null);
            }

            T data = parser.parse(body.string());
            return ApiResponse.success(data, response.code());

        } catch (SocketTimeoutException e) {
            logger.error("Request timeout: {}", request.url(), e);
            return ApiResponse.error("Request timeout", 408, e);

        } catch (IOException e) {
            logger.error("Network error: {}", request.url(), e);
            return ApiResponse.error("Network error: " + e.getMessage(),
                -1, e);

        } catch (Exception e) {
            logger.error("Unexpected error: {}", request.url(), e);
            return ApiResponse.error("Unexpected error: " + e.getMessage(),
                -1, e);
        }
    }

    private <T> ApiResponse<T> handleErrorResponse(Response response) {
        int code = response.code();
        String message;

        try {
            ResponseBody errorBody = response.body();
            message = errorBody != null ? errorBody.string() : "Unknown error";
        } catch (IOException e) {
            message = "Failed to read error body";
        }

        switch (code) {
            case 400:
                return ApiResponse.error("Bad Request: " + message, code, null);
            case 401:
                return ApiResponse.error("Unauthorized: " + message, code, null);
            case 403:
                return ApiResponse.error("Forbidden: " + message, code, null);
            case 404:
                return ApiResponse.error("Not Found: " + message, code, null);
            case 429:
                return ApiResponse.error("Rate Limited: " + message, code, null);
            case 500:
                return ApiResponse.error("Server Error: " + message, code, null);
            case 503:
                return ApiResponse.error("Service Unavailable: " + message, code, null);
            default:
                return ApiResponse.error("HTTP Error " + code + ": " + message,
                    code, null);
        }
    }

    @FunctionalInterface
    public interface ResponseParser<T> {
        T parse(String body) throws Exception;
    }
}

/**
 * Generic API Response wrapper
 */
class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int statusCode;
    private final Exception exception;

    private ApiResponse(boolean success, T data, String errorMessage,
                       int statusCode, Exception exception) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.exception = exception;
    }

    public static <T> ApiResponse<T> success(T data, int statusCode) {
        return new ApiResponse<>(true, data, null, statusCode, null);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode,
                                          Exception exception) {
        return new ApiResponse<>(false, null, message, statusCode, exception);
    }

    public boolean isSuccess() {
        return success;
    }

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }
}
```

## Retry & Circuit Breaking

### Retry Interceptor

```java
package com.example.http.retry;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Implements exponential backoff retry logic
 */
public class RetryInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);

    private final int maxRetries;
    private final long initialDelayMs;
    private final double backoffMultiplier;

    public RetryInterceptor(int maxRetries, long initialDelayMs,
                          double backoffMultiplier) {
        this.maxRetries = maxRetries;
        this.initialDelayMs = initialDelayMs;
        this.backoffMultiplier = backoffMultiplier;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;

        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                if (response != null) {
                    response.close();
                }

                response = chain.proceed(request);

                // Don't retry on successful responses or client errors
                if (response.isSuccessful() || response.code() < 500) {
                    return response;
                }

                // Server errors might be retryable
                if (attempt < maxRetries && isRetryable(response.code())) {
                    response.close();
                    waitBeforeRetry(attempt);
                    attempt++;
                    logger.warn("Retrying request (attempt {}/{}): {}",
                        attempt, maxRetries, request.url());
                    continue;
                }

                return response;

            } catch (IOException e) {
                lastException = e;

                if (attempt < maxRetries && isRetryableException(e)) {
                    waitBeforeRetry(attempt);
                    attempt++;
                    logger.warn("Retrying request after exception (attempt {}/{}): {}",
                        attempt, maxRetries, e.getMessage());
                } else {
                    throw e;
                }
            }
        }

        if (lastException != null) {
            throw lastException;
        }

        return response;
    }

    private boolean isRetryable(int statusCode) {
        return statusCode == 503 || statusCode == 502 || statusCode == 504;
    }

    private boolean isRetryableException(IOException e) {
        // Retry on network errors, but not on protocol errors
        return !(e instanceof java.net.ProtocolException);
    }

    private void waitBeforeRetry(int attempt) {
        long delay = (long) (initialDelayMs * Math.pow(backoffMultiplier, attempt));
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### Circuit Breaker Implementation

```java
package com.example.http.circuitbreaker;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Circuit breaker pattern implementation for OkHttp
 */
public class CircuitBreakerInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerInterceptor.class);

    private enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private final int failureThreshold;
    private final long timeout;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicLong openedTime = new AtomicLong(0);
    private volatile State state = State.CLOSED;

    public CircuitBreakerInterceptor(int failureThreshold, long timeoutMs) {
        this.failureThreshold = failureThreshold;
        this.timeout = timeoutMs;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - openedTime.get() > timeout) {
                logger.info("Circuit breaker transitioning to HALF_OPEN");
                state = State.HALF_OPEN;
            } else {
                throw new CircuitBreakerOpenException(
                    "Circuit breaker is OPEN - failing fast");
            }
        }

        try {
            Response response = chain.proceed(chain.request());

            if (response.isSuccessful()) {
                onSuccess();
            } else if (response.code() >= 500) {
                onFailure();
            }

            return response;

        } catch (IOException e) {
            onFailure();
            throw e;
        }
    }

    private synchronized void onSuccess() {
        if (state == State.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= failureThreshold / 2) {
                logger.info("Circuit breaker transitioning to CLOSED");
                state = State.CLOSED;
                failureCount.set(0);
                successCount.set(0);
            }
        } else if (state == State.CLOSED) {
            failureCount.set(0);
        }
    }

    private synchronized void onFailure() {
        failureCount.incrementAndGet();

        if (state == State.HALF_OPEN) {
            logger.warn("Circuit breaker transitioning to OPEN from HALF_OPEN");
            state = State.OPEN;
            openedTime.set(System.currentTimeMillis());
            successCount.set(0);
        } else if (state == State.CLOSED &&
                   failureCount.get() >= failureThreshold) {
            logger.warn("Circuit breaker transitioning to OPEN - " +
                       "failure threshold reached: {}", failureThreshold);
            state = State.OPEN;
            openedTime.set(System.currentTimeMillis());
        }
    }

    public State getState() {
        return state;
    }

    public int getFailureCount() {
        return failureCount.get();
    }

    public static class CircuitBreakerOpenException extends IOException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}
```

## Request Builder Utilities

### Fluent Request Builder

```java
package com.example.http;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Fluent API for building HTTP requests
 */
public class RequestBuilder {

    private static final MediaType JSON = MediaType.parse("application/json");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private String method = "GET";
    private RequestBody body;

    private RequestBuilder(String url) {
        this.url = url;
    }

    public static RequestBuilder create(String url) {
        return new RequestBuilder(url);
    }

    public RequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder get() {
        return method("GET");
    }

    public RequestBuilder post() {
        return method("POST");
    }

    public RequestBuilder put() {
        return method("PUT");
    }

    public RequestBuilder delete() {
        return method("DELETE");
    }

    public RequestBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestBuilder queryParam(String name, String value) {
        queryParams.put(name, value);
        return this;
    }

    public RequestBuilder jsonBody(Object object) throws IOException {
        String json = objectMapper.writeValueAsString(object);
        this.body = RequestBody.create(json, JSON);
        return this;
    }

    public RequestBuilder formBody(Map<String, String> formData) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formData.forEach(formBuilder::add);
        this.body = formBuilder.build();
        return this;
    }

    public RequestBuilder multipartBody(Map<String, Object> parts) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
            .setType(MultipartBody.FORM);

        parts.forEach((key, value) -> {
            if (value instanceof File) {
                File file = (File) value;
                multipartBuilder.addFormDataPart(key, file.getName(),
                    RequestBody.create(file, MediaType.parse("application/octet-stream")));
            } else {
                multipartBuilder.addFormDataPart(key, value.toString());
            }
        });

        this.body = multipartBuilder.build();
        return this;
    }

    public Request build() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        queryParams.forEach(urlBuilder::addQueryParameter);

        Request.Builder requestBuilder = new Request.Builder()
            .url(urlBuilder.build())
            .method(method, body);

        headers.forEach(requestBuilder::header);

        return requestBuilder.build();
    }
}
```

## Async Request Handler

### Asynchronous Request Execution

```java
package com.example.http;

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
public class AsyncHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClient.class);

    private final OkHttpClient client;
    private final ExecutorService executorService;

    public AsyncHttpClient(OkHttpClient client) {
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
```

## Rate Limiting

### Rate Limiter Implementation

```java
package com.example.http.ratelimit;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate limiting interceptor using token bucket algorithm
 */
public class RateLimitInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private final Semaphore semaphore;
    private final int maxRequests;
    private final long refillIntervalMs;
    private final AtomicLong lastRefillTime = new AtomicLong(System.currentTimeMillis());

    public RateLimitInterceptor(int maxRequestsPerSecond) {
        this.maxRequests = maxRequestsPerSecond;
        this.refillIntervalMs = 1000;
        this.semaphore = new Semaphore(maxRequestsPerSecond);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        refillTokens();

        try {
            if (!semaphore.tryAcquire(5, TimeUnit.SECONDS)) {
                throw new RateLimitExceededException(
                    "Rate limit exceeded: " + maxRequests + " requests per second"
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while waiting for rate limit", e);
        }

        try {
            return chain.proceed(chain.request());
        } finally {
            // Tokens are refilled on a schedule, not released immediately
        }
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long lastRefill = lastRefillTime.get();

        if (now - lastRefill >= refillIntervalMs) {
            if (lastRefillTime.compareAndSet(lastRefill, now)) {
                int availablePermits = semaphore.availablePermits();
                int permitsToAdd = maxRequests - availablePermits;

                if (permitsToAdd > 0) {
                    semaphore.release(permitsToAdd);
                    logger.debug("Refilled {} rate limit tokens", permitsToAdd);
                }
            }
        }
    }

    public static class RateLimitExceededException extends IOException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
```

## Caching

### Advanced Caching Strategy

```java
package com.example.http.cache;

import okhttp3.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Configures HTTP caching for OkHttp
 */
public class CacheConfiguration {

    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    /**
     * Creates client with disk cache
     */
    public static OkHttpClient createCachedClient(File cacheDir) {
        Cache cache = new Cache(cacheDir, CACHE_SIZE);

        return new OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(new CacheControlInterceptor())
            .build();
    }

    /**
     * Cache control interceptor
     */
    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws java.io.IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            // Cache GET requests for 5 minutes
            if ("GET".equals(request.method())) {
                return response.newBuilder()
                    .header("Cache-Control",
                        "public, max-age=" + TimeUnit.MINUTES.toSeconds(5))
                    .removeHeader("Pragma")
                    .build();
            }

            return response;
        }
    }

    /**
     * Force cache for offline scenarios
     */
    public static Request forceCacheRequest(Request original) {
        return original.newBuilder()
            .cacheControl(new CacheControl.Builder()
                .onlyIfCached()
                .maxStale(7, TimeUnit.DAYS)
                .build())
            .build();
    }

    /**
     * Force network (bypass cache)
     */
    public static Request forceNetworkRequest(Request original) {
        return original.newBuilder()
            .cacheControl(new CacheControl.Builder()
                .noCache()
                .build())
            .build();
    }
}
```

## Complete Production Example

### Production Service Implementation

```java
package com.example.http.service;

import com.example.http.*;
import com.example.http.interceptors.*;
import com.example.http.retry.*;
import com.example.http.circuitbreaker.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Complete production-ready HTTP service
 */
public class ProductionHttpService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionHttpService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final OkHttpClient client;
    private final AsyncHttpClient asyncClient;
    private final String baseUrl;

    public ProductionHttpService(String baseUrl, File cacheDir) {
        this.baseUrl = baseUrl;
        this.client = buildProductionClient(cacheDir);
        this.asyncClient = new AsyncHttpClient(client);
    }

    private OkHttpClient buildProductionClient(File cacheDir) {
        // Connection pool
        ConnectionPool connectionPool = new ConnectionPool(
            50,                         // max idle connections
            5,                          // keep alive duration
            TimeUnit.MINUTES
        );

        // Cache
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        // Build client
        return new OkHttpClient.Builder()
            // Basic configuration
            .connectionPool(connectionPool)
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

            // Interceptors
            .addInterceptor(new HeaderInterceptor())
            .addInterceptor(new AuthInterceptor())
            .addInterceptor(new MetricsInterceptor())
            .addInterceptor(new RetryInterceptor(3, 1000, 2.0))
            .addInterceptor(new CircuitBreakerInterceptor(5, 60000))
            .addNetworkInterceptor(new CompressionInterceptor())

            // Logging (disable in production or use appropriate level)
            .addInterceptor(createLoggingInterceptor())

            .build();
    }

    private Interceptor createLoggingInterceptor() {
        okhttp3.logging.HttpLoggingInterceptor logging =
            new okhttp3.logging.HttpLoggingInterceptor(logger::debug);

        // Use BASIC in production, BODY for development
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BASIC);

        return logging;
    }

    /**
     * GET request
     */
    public <T> T get(String path, Class<T> responseType) throws IOException {
        Request request = RequestBuilder.create(baseUrl + path)
            .get()
            .build();

        return executeSync(request, responseType);
    }

    /**
     * POST request
     */
    public <T> T post(String path, Object requestBody, Class<T> responseType)
            throws IOException {
        Request request = RequestBuilder.create(baseUrl + path)
            .post()
            .jsonBody(requestBody)
            .build();

        return executeSync(request, responseType);
    }

    /**
     * PUT request
     */
    public <T> T put(String path, Object requestBody, Class<T> responseType)
            throws IOException {
        Request request = RequestBuilder.create(baseUrl + path)
            .put()
            .jsonBody(requestBody)
            .build();

        return executeSync(request, responseType);
    }

    /**
     * DELETE request
     */
    public <T> T delete(String path, Class<T> responseType) throws IOException {
        Request request = RequestBuilder.create(baseUrl + path)
            .delete()
            .build();

        return executeSync(request, responseType);
    }

    /**
     * Async GET request
     */
    public <T> CompletableFuture<T> getAsync(String path, Class<T> responseType) {
        Request request = RequestBuilder.create(baseUrl + path)
            .get()
            .build();

        return executeAsync(request, responseType);
    }

    /**
     * Async POST request
     */
    public <T> CompletableFuture<T> postAsync(
            String path,
            Object requestBody,
            Class<T> responseType) throws IOException {

        Request request = RequestBuilder.create(baseUrl + path)
            .post()
            .jsonBody(requestBody)
            .build();

        return executeAsync(request, responseType);
    }

    private <T> T executeSync(Request request, Class<T> responseType)
            throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Empty response body");
            }

            return objectMapper.readValue(body.string(), responseType);
        }
    }

    private <T> CompletableFuture<T> executeAsync(
            Request request,
            Class<T> responseType) {

        return asyncClient.executeAsync(request,
            body -> objectMapper.readValue(body, responseType));
    }

    /**
     * Upload file
     */
    public <T> T uploadFile(
            String path,
            File file,
            Class<T> responseType) throws IOException {

        RequestBody fileBody = RequestBody.create(
            file,
            MediaType.parse("application/octet-stream")
        );

        MultipartBody multipartBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.getName(), fileBody)
            .build();

        Request request = new Request.Builder()
            .url(baseUrl + path)
            .post(multipartBody)
            .build();

        return executeSync(request, responseType);
    }

    /**
     * Download file
     */
    public File downloadFile(String path, File destination) throws IOException {
        Request request = new Request.Builder()
            .url(baseUrl + path)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Empty response body");
            }

            okio.BufferedSink sink = okio.Okio.buffer(okio.Okio.sink(destination));
            sink.writeAll(body.source());
            sink.close();

            return destination;
        }
    }

    /**
     * Health check
     */
    public boolean healthCheck() {
        try {
            Request request = RequestBuilder.create(baseUrl + "/health")
                .get()
                .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Health check failed", e);
            return false;
        }
    }

    /**
     * Get cache statistics
     */
    public CacheStats getCacheStats() {
        Cache cache = client.cache();
        if (cache == null) {
            return new CacheStats(0, 0, 0, 0);
        }

        try {
            return new CacheStats(
                cache.requestCount(),
                cache.hitCount(),
                cache.networkCount(),
                cache.size()
            );
        } catch (IOException e) {
            logger.error("Failed to get cache stats", e);
            return new CacheStats(0, 0, 0, 0);
        }
    }

    /**
     * Shutdown service
     */
    public void shutdown() {
        asyncClient.shutdown();

        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();

        Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
                logger.error("Error closing cache", e);
            }
        }
    }

    public static class CacheStats {
        private final int requestCount;
        private final int hitCount;
        private final int networkCount;
        private final long size;

        public CacheStats(int requestCount, int hitCount, int networkCount, long size) {
            this.requestCount = requestCount;
            this.hitCount = hitCount;
            this.networkCount = networkCount;
            this.size = size;
        }

        public int getRequestCount() { return requestCount; }
        public int getHitCount() { return hitCount; }
        public int getNetworkCount() { return networkCount; }
        public long getSize() { return size; }

        public double getHitRate() {
            return requestCount > 0 ? (double) hitCount / requestCount : 0.0;
        }

        @Override
        public String toString() {
            return String.format(
                "CacheStats{requests=%d, hits=%d, network=%d, hitRate=%.2f%%, size=%d bytes}",
                requestCount, hitCount, networkCount, getHitRate() * 100, size
            );
        }
    }
}
```

## Testing

### Unit Tests with MockWebServer

```java
package com.example.http;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests using MockWebServer
 */
public class ProductionHttpServiceTest {

    private MockWebServer mockWebServer;
    private ProductionHttpService service;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        File cacheDir = new File("test-cache");
        service = new ProductionHttpService(baseUrl, cacheDir);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
        service.shutdown();
    }

    @Test
    public void testGetRequest() throws IOException, InterruptedException {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"id\":1,\"name\":\"Test\"}")
            .addHeader("Content-Type", "application/json"));

        // Execute request
        TestResponse response = service.get("/api/test", TestResponse.class);

        // Verify
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Test", response.getName());

        // Verify request
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/api/test", request.getPath());
    }

    @Test
    public void testPostRequest() throws IOException, InterruptedException {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(201)
            .setBody("{\"id\":2,\"name\":\"Created\"}")
            .addHeader("Content-Type", "application/json"));

        // Execute request
        TestRequest requestBody = new TestRequest("New Item");
        TestResponse response = service.post("/api/test", requestBody, TestResponse.class);

        // Verify
        assertNotNull(response);
        assertEquals(2, response.getId());
        assertEquals("Created", response.getName());

        // Verify request
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getBody().readUtf8().contains("New Item"));
    }

    @Test
    public void testRetryOnFailure() throws IOException {
        // Setup mock responses - first fails, second succeeds
        mockWebServer.enqueue(new MockResponse().setResponseCode(503));
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"id\":1,\"name\":\"Success\"}")
            .addHeader("Content-Type", "application/json"));

        // Execute request
        TestResponse response = service.get("/api/test", TestResponse.class);

        // Verify
        assertNotNull(response);
        assertEquals("Success", response.getName());
    }

    @Test(expected = IOException.class)
    public void testErrorHandling() throws IOException {
        // Setup mock response with error
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        // This should throw an exception
        service.get("/api/test", TestResponse.class);
    }

    // Test DTOs
    private static class TestRequest {
        private String name;

        public TestRequest(String name) {
            this.name = name;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    private static class TestResponse {
        private int id;
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
```

## Production Best Practices

### Configuration Management

```java
package com.example.http.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * HTTP client configuration from properties file
 */
public class HttpClientConfig {

    private final int maxIdleConnections;
    private final int keepAliveDurationMs;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final int writeTimeoutMs;
    private final int callTimeoutMs;
    private final boolean retryOnFailure;
    private final int maxRetries;
    private final int rateLimitPerSecond;
    private final int circuitBreakerThreshold;
    private final long circuitBreakerTimeoutMs;

    private HttpClientConfig(Properties props) {
        this.maxIdleConnections = getInt(props, "http.pool.maxIdle", 50);
        this.keepAliveDurationMs = getInt(props, "http.pool.keepAlive", 300000);
        this.connectTimeoutMs = getInt(props, "http.timeout.connect", 10000);
        this.readTimeoutMs = getInt(props, "http.timeout.read", 30000);
        this.writeTimeoutMs = getInt(props, "http.timeout.write", 30000);
        this.callTimeoutMs = getInt(props, "http.timeout.call", 60000);
        this.retryOnFailure = getBoolean(props, "http.retry.enabled", true);
        this.maxRetries = getInt(props, "http.retry.maxAttempts", 3);
        this.rateLimitPerSecond = getInt(props, "http.rateLimit.perSecond", 100);
        this.circuitBreakerThreshold = getInt(props, "http.circuitBreaker.threshold", 5);
        this.circuitBreakerTimeoutMs = getLong(props, "http.circuitBreaker.timeout", 60000);
    }

    public static HttpClientConfig fromFile(String filename) throws IOException {
        Properties props = new Properties();
        try (InputStream input = HttpClientConfig.class.getClassLoader()
                .getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Unable to find " + filename);
            }
            props.load(input);
        }
        return new HttpClientConfig(props);
    }

    public static HttpClientConfig defaults() {
        return new HttpClientConfig(new Properties());
    }

    private int getInt(Properties props, String key, int defaultValue) {
        return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
    }

    private long getLong(Properties props, String key, long defaultValue) {
        return Long.parseLong(props.getProperty(key, String.valueOf(defaultValue)));
    }

    private boolean getBoolean(Properties props, String key, boolean defaultValue) {
        return Boolean.parseBoolean(props.getProperty(key, String.valueOf(defaultValue)));
    }

    // Getters
    public int getMaxIdleConnections() { return maxIdleConnections; }
    public int getKeepAliveDurationMs() { return keepAliveDurationMs; }
    public int getConnectTimeoutMs() { return connectTimeoutMs; }
    public int getReadTimeoutMs() { return readTimeoutMs; }
    public int getWriteTimeoutMs() { return writeTimeoutMs; }
    public int getCallTimeoutMs() { return callTimeoutMs; }
    public boolean isRetryOnFailure() { return retryOnFailure; }
    public int getMaxRetries() { return maxRetries; }
    public int getRateLimitPerSecond() { return rateLimitPerSecond; }
    public int getCircuitBreakerThreshold() { return circuitBreakerThreshold; }
    public long getCircuitBreakerTimeoutMs() { return circuitBreakerTimeoutMs; }
}
```

### Example Properties File

```properties
# http-client.properties

# Connection Pool
http.pool.maxIdle=50
http.pool.keepAlive=300000

# Timeouts (milliseconds)
http.timeout.connect=10000
http.timeout.read=30000
http.timeout.write=30000
http.timeout.call=60000

# Retry Configuration
http.retry.enabled=true
http.retry.maxAttempts=3

# Rate Limiting
http.rateLimit.perSecond=100

# Circuit Breaker
http.circuitBreaker.threshold=5
http.circuitBreaker.timeout=60000
```

## Monitoring and Metrics

### Metrics Collection

```java
package com.example.http.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Collects and reports HTTP client metrics
 */
public class HttpMetrics {

    private static final HttpMetrics INSTANCE = new HttpMetrics();

    private final ConcurrentHashMap<String, EndpointMetrics> metricsMap =
        new ConcurrentHashMap<>();

    private HttpMetrics() {}

    public static HttpMetrics getInstance() {
        return INSTANCE;
    }

    public void recordRequest(String endpoint, int statusCode, long durationMs) {
        EndpointMetrics metrics = metricsMap.computeIfAbsent(
            endpoint,
            k -> new EndpointMetrics()
        );

        metrics.recordRequest(statusCode, durationMs);
    }

    public EndpointMetrics getMetrics(String endpoint) {
        return metricsMap.get(endpoint);
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("HTTP Client Metrics Report\n");
        report.append("==========================\n\n");

        metricsMap.forEach((endpoint, metrics) -> {
            report.append(String.format("Endpoint: %s\n", endpoint));
            report.append(metrics.toString());
            report.append("\n");
        });

        return report.toString();
    }

    public static class EndpointMetrics {
        private final LongAdder totalRequests = new LongAdder();
        private final LongAdder successfulRequests = new LongAdder();
        private final LongAdder failedRequests = new LongAdder();
        private final LongAdder totalDuration = new LongAdder();
        private final AtomicLong minDuration = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxDuration = new AtomicLong(Long.MIN_VALUE);

        public void recordRequest(int statusCode, long durationMs) {
            totalRequests.increment();
            totalDuration.add(durationMs);

            if (statusCode >= 200 && statusCode < 300) {
                successfulRequests.increment();
            } else {
                failedRequests.increment();
            }

            updateMin(durationMs);
            updateMax(durationMs);
        }

        private void updateMin(long duration) {
            long current;
            while (duration < (current = minDuration.get())) {
                if (minDuration.compareAndSet(current, duration)) {
                    break;
                }
            }
        }

        private void updateMax(long duration) {
            long current;
            while (duration > (current = maxDuration.get())) {
                if (maxDuration.compareAndSet(current, duration)) {
                    break;
                }
            }
        }

        public long getTotalRequests() {
            return totalRequests.sum();
        }

        public long getSuccessfulRequests() {
            return successfulRequests.sum();
        }

        public long getFailedRequests() {
            return failedRequests.sum();
        }

        public double getSuccessRate() {
            long total = getTotalRequests();
            return total > 0 ? (double) getSuccessfulRequests() / total * 100 : 0;
        }

        public double getAverageDuration() {
            long total = getTotalRequests();
            return total > 0 ? (double) totalDuration.sum() / total : 0;
        }

        public long getMinDuration() {
            return minDuration.get() == Long.MAX_VALUE ? 0 : minDuration.get();
        }

        public long getMaxDuration() {
            return maxDuration.get() == Long.MIN_VALUE ? 0 : maxDuration.get();
        }

        @Override
        public String toString() {
            return String.format(
                "  Total Requests: %d\n" +
                "  Successful: %d (%.2f%%)\n" +
                "  Failed: %d (%.2f%%)\n" +
                "  Avg Duration: %.2fms\n" +
                "  Min Duration: %dms\n" +
                "  Max Duration: %dms",
                getTotalRequests(),
                getSuccessfulRequests(), getSuccessRate(),
                getFailedRequests(), 100 - getSuccessRate(),
                getAverageDuration(),
                getMinDuration(),
                getMaxDuration()
            );
        }
    }
}
```

This comprehensive guide covers all essential aspects of using OkHttp in production environments, including configuration, error handling, retry logic, circuit breaking, caching, monitoring, and testing. The code examples are production-ready and follow best practices for scalability, reliability, and maintainability.
