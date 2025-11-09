# Comprehensive Guide to OkHttp 5.x for Production Environments

## Table of Contents
1. [Introduction](#introduction)
2. [Setup and Configuration](#setup-and-configuration)
3. [Basic Usage](#basic-usage)
4. [Advanced Features](#advanced-features)
5. [Production Best Practices](#production-best-practices)
6. [Error Handling](#error-handling)
7. [Testing](#testing)
8. [Performance Optimization](#performance-optimization)

## Introduction

OkHttp is an efficient HTTP & HTTP/2 client for Android and Java applications. This guide covers production-ready implementations for OkHttp 5.x.

## Setup and Configuration

### Maven Dependency

```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>5.0.0-alpha.12</version>
</dependency>

<!-- Optional: Logging interceptor -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>logging-interceptor</artifactId>
    <version>5.0.0-alpha.12</version>
</dependency>

<!-- Optional: Mockwebserver for testing -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>mockwebserver</artifactId>
    <version>5.0.0-alpha.12</version>
    <scope>test</scope>
</dependency>
```

### Gradle Dependency

```gradle
implementation 'com.squareup.okhttp3:okhttp:5.0.0-alpha.12'
implementation 'com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.12'
testImplementation 'com.squareup.okhttp3:mockwebserver:5.0.0-alpha.12'
```

## Basic Usage

### 1. Production-Ready OkHttpClient Configuration

```java
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class OkHttpClientFactory {

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;
    private static final int MAX_IDLE_CONNECTIONS = 5;
    private static final int KEEP_ALIVE_DURATION = 5;

    private static volatile OkHttpClient instance;

    private OkHttpClientFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Thread-safe singleton instance with double-checked locking
     */
    public static OkHttpClient getInstance() {
        if (instance == null) {
            synchronized (OkHttpClientFactory.class) {
                if (instance == null) {
                    instance = createClient();
                }
            }
        }
        return instance;
    }

    /**
     * Creates a production-ready OkHttpClient with recommended settings
     */
    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                // Timeouts
                .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                .readTimeout(Duration.ofSeconds(READ_TIMEOUT))
                .writeTimeout(Duration.ofSeconds(WRITE_TIMEOUT))
                .callTimeout(Duration.ofSeconds(90)) // Overall timeout

                // Connection pool
                .connectionPool(new ConnectionPool(
                        MAX_IDLE_CONNECTIONS,
                        KEEP_ALIVE_DURATION,
                        TimeUnit.MINUTES
                ))

                // Interceptors
                .addInterceptor(new RetryInterceptor(3))
                .addInterceptor(new AuthenticationInterceptor())
                .addInterceptor(createLoggingInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())

                // Cache
                .cache(createCache())

                // Protocols
                .protocols(java.util.List.of(Protocol.HTTP_2, Protocol.HTTP_1_1))

                // Follow redirects
                .followRedirects(true)
                .followSslRedirects(true)

                // Retry on connection failure
                .retryOnConnectionFailure(true)

                .build();
    }

    /**
     * Create cache for HTTP responses
     */
    private static Cache createCache() {
        // 10 MB cache
        return new Cache(
                new java.io.File(System.getProperty("java.io.tmpdir"), "okhttp-cache"),
                10 * 1024 * 1024
        );
    }

    /**
     * Configure logging based on environment
     */
    private static HttpLoggingInterceptor createLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                message -> System.out.println("[OkHttp] " + message)
        );

        // Use BASIC or NONE in production
        String env = System.getProperty("app.environment", "production");
        if ("development".equals(env)) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        return logging;
    }
}
```

### 2. Custom Interceptors

```java
/**
 * Retry interceptor for handling transient failures
 */
class RetryInterceptor implements Interceptor {
    private final int maxRetries;

    public RetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                response = chain.proceed(request);

                // Don't retry on successful response
                if (response.isSuccessful()) {
                    return response;
                }

                // Close unsuccessful response
                if (response != null) {
                    response.close();
                }

                // Only retry on specific status codes
                if (!shouldRetry(response)) {
                    return response;
                }

                // Exponential backoff
                if (attempt < maxRetries - 1) {
                    Thread.sleep((long) Math.pow(2, attempt) * 1000);
                }

            } catch (IOException e) {
                lastException = e;
                if (attempt < maxRetries - 1) {
                    try {
                        Thread.sleep((long) Math.pow(2, attempt) * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Retry interrupted", e);
            }
        }

        if (lastException != null) {
            throw lastException;
        }

        return response;
    }

    private boolean shouldRetry(Response response) {
        if (response == null) return true;

        int code = response.code();
        // Retry on 429 (Too Many Requests), 500, 502, 503, 504
        return code == 429 || code == 500 || code == 502 ||
               code == 503 || code == 504;
    }
}
```

```java
/**
 * Authentication interceptor for adding auth headers
 */
class AuthenticationInterceptor implements Interceptor {
    private volatile String authToken;

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (authToken == null || authToken.isEmpty()) {
            return chain.proceed(original);
        }

        Request authenticated = original.newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .header("User-Agent", "MyApp/1.0")
                .build();

        Response response = chain.proceed(authenticated);

        // Handle token expiration
        if (response.code() == 401) {
            response.close();
            // Refresh token logic here
            String newToken = refreshToken();
            if (newToken != null) {
                this.authToken = newToken;
                Request retryRequest = original.newBuilder()
                        .header("Authorization", "Bearer " + newToken)
                        .build();
                return chain.proceed(retryRequest);
            }
        }

        return response;
    }

    private String refreshToken() {
        // Implement token refresh logic
        return null;
    }
}
```

```java
/**
 * Cache interceptor for controlling cache behavior
 */
class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Customize cache control based on request
        if (request.header("Cache-Control") == null) {
            request = request.newBuilder()
                    .header("Cache-Control", "public, max-age=300")
                    .build();
        }

        Response response = chain.proceed(request);

        // Customize response cache headers
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=300")
                .removeHeader("Pragma")
                .build();
    }
}
```

### 3. HTTP Request Examples

```java
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpRequestExamples {

    private final OkHttpClient client;

    public HttpRequestExamples() {
        this.client = OkHttpClientFactory.getInstance();
    }

    /**
     * Synchronous GET request
     */
    public String getSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody body = response.body();
            return body != null ? body.string() : null;
        }
    }

    /**
     * Asynchronous GET request with callback
     */
    public void getAsync(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * Asynchronous GET request with CompletableFuture
     */
    public CompletableFuture<String> getAsyncFuture(String url) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(
                            new IOException("Unexpected code " + response)
                        );
                        return;
                    }

                    String result = body != null ? body.string() : null;
                    future.complete(result);
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * POST request with JSON body
     */
    public String postJson(String url, String json) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * POST request with form data
     */
    public String postForm(String url, java.util.Map<String, String> formData)
            throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formData.forEach(formBuilder::add);

        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * Multipart file upload
     */
    public String uploadFile(String url, java.io.File file, String description)
            throws IOException {
        RequestBody fileBody = RequestBody.create(
            file,
            MediaType.parse("application/octet-stream")
        );

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("description", description)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * PUT request
     */
    public String put(String url, String json) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * PATCH request
     */
    public String patch(String url, String json) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * DELETE request
     */
    public boolean delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * HEAD request to check resource existence
     */
    public boolean exists(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .head()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
}
```

## Advanced Features

### 1. Response Streaming and Download

```java
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StreamingExamples {

    private final OkHttpClient client;

    public StreamingExamples() {
        this.client = OkHttpClientFactory.getInstance();
    }

    /**
     * Download file with progress tracking
     */
    public void downloadFile(String url, File destination,
                            ProgressListener listener) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download: " + response);
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Response body is null");
            }

            long contentLength = body.contentLength();

            try (InputStream input = body.byteStream();
                 BufferedSink sink = Okio.buffer(Okio.sink(destination))) {

                byte[] buffer = new byte[8192];
                long downloaded = 0;
                int read;

                while ((read = input.read(buffer)) != -1) {
                    sink.write(buffer, 0, read);
                    downloaded += read;

                    if (listener != null) {
                        listener.onProgress(downloaded, contentLength);
                    }
                }

                sink.flush();
            }
        }
    }

    /**
     * Stream large response
     */
    public void streamResponse(String url, ResponseHandler handler)
            throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Response body is null");
            }

            try (InputStream stream = body.byteStream()) {
                handler.handleStream(stream);
            }
        }
    }

    public interface ProgressListener {
        void onProgress(long bytesRead, long contentLength);
    }

    public interface ResponseHandler {
        void handleStream(InputStream stream) throws IOException;
    }
}
```

### 2. WebSocket Implementation

```java
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WebSocketExample {

    private final OkHttpClient client;
    private WebSocket webSocket;

    public WebSocketExample() {
        // WebSocket specific client configuration
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
    }

    public void connect(String url, WebSocketListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = client.newWebSocket(request, listener);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye");
        }
    }

    /**
     * Example WebSocket listener implementation
     */
    public static class CustomWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("WebSocket opened");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Received: " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, okio.ByteString bytes) {
            System.out.println("Received bytes: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("Closing: " + code + " / " + reason);
            webSocket.close(1000, null);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            System.out.println("Closed: " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.err.println("Error: " + t.getMessage());
        }
    }
}
```

### 3. Request/Response Customization

```java
import okhttp3.*;

import java.io.IOException;

public class CustomRequestResponse {

    private final OkHttpClient client;

    public CustomRequestResponse() {
        this.client = OkHttpClientFactory.getInstance();
    }

    /**
     * Request with custom headers
     */
    public Response customHeaders(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Accept-Language", "en-US")
                .header("Custom-Header", "custom-value")
                .addHeader("Multi-Value", "value1")
                .addHeader("Multi-Value", "value2")
                .build();

        return client.newCall(request).execute();
    }

    /**
     * Request with URL parameters
     */
    public Response withUrlParams(String baseUrl) throws IOException {
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("page", "1")
                .addQueryParameter("limit", "10")
                .addQueryParameter("sort", "desc")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return client.newCall(request).execute();
    }

    /**
     * Handle response headers
     */
    public void processResponseHeaders(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Headers headers = response.headers();

            System.out.println("Headers:");
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i) + ": " + headers.value(i));
            }

            // Access specific headers
            String contentType = response.header("Content-Type");
            String cacheControl = response.header("Cache-Control");

            System.out.println("Content-Type: " + contentType);
            System.out.println("Cache-Control: " + cacheControl);
        }
    }

    /**
     * Handle cookies
     */
    public void handleCookies(String url) throws IOException {
        // Create a cookie jar
        CookieJar cookieJar = new CookieJar() {
            private final java.util.HashMap<String, java.util.List<Cookie>>
                    cookieStore = new java.util.HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, java.util.List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public java.util.List<Cookie> loadForRequest(HttpUrl url) {
                java.util.List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new java.util.ArrayList<>();
            }
        };

        OkHttpClient clientWithCookies = client.newBuilder()
                .cookieJar(cookieJar)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = clientWithCookies.newCall(request).execute()) {
            System.out.println("Response received with cookies");
        }
    }
}
```

## Production Best Practices

### 1. Connection Pool Management

```java
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolConfiguration {

    /**
     * Create client with optimized connection pool for high-traffic scenarios
     */
    public static OkHttpClient createHighTrafficClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(
                        50,  // max idle connections
                        5,   // keep alive duration
                        TimeUnit.MINUTES
                ))
                .build();
    }

    /**
     * Create client with minimal connection pool for low-traffic scenarios
     */
    public static OkHttpClient createLowTrafficClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(
                        5,   // max idle connections
                        2,   // keep alive duration
                        TimeUnit.MINUTES
                ))
                .build();
    }
}
```

### 2. Error Handling and Resilience

```java
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

public class ResilientHttpClient {

    private final OkHttpClient client;

    public ResilientHttpClient() {
        this.client = OkHttpClientFactory.getInstance();
    }

    /**
     * Wrapper for safe HTTP execution with proper error handling
     */
    public <T> Result<T> executeRequest(Request request,
                                       ResponseParser<T> parser) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return Result.failure(
                    new HttpException(response.code(), response.message())
                );
            }

            ResponseBody body = response.body();
            if (body == null) {
                return Result.failure(
                    new HttpException("Response body is null")
                );
            }

            T result = parser.parse(body.string());
            return Result.success(result);

        } catch (IOException e) {
            return Result.failure(e);
        } catch (Exception e) {
            return Result.failure(
                new HttpException("Unexpected error: " + e.getMessage(), e)
            );
        }
    }

    /**
     * Async execution with result callback
     */
    public <T> void executeAsync(Request request,
                                ResponseParser<T> parser,
                                ResultCallback<T> callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onResult(Result.failure(e));
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        callback.onResult(Result.failure(
                            new HttpException(response.code(), response.message())
                        ));
                        return;
                    }

                    if (body == null) {
                        callback.onResult(Result.failure(
                            new HttpException("Response body is null")
                        ));
                        return;
                    }

                    T result = parser.parse(body.string());
                    callback.onResult(Result.success(result));

                } catch (Exception e) {
                    callback.onResult(Result.failure(e));
                }
            }
        });
    }

    @FunctionalInterface
    public interface ResponseParser<T> {
        T parse(String body) throws Exception;
    }

    @FunctionalInterface
    public interface ResultCallback<T> {
        void onResult(Result<T> result);
    }

    /**
     * Result wrapper for type-safe error handling
     */
    public static class Result<T> {
        private final T data;
        private final Exception error;

        private Result(T data, Exception error) {
            this.data = data;
            this.error = error;
        }

        public static <T> Result<T> success(T data) {
            return new Result<>(data, null);
        }

        public static <T> Result<T> failure(Exception error) {
            return new Result<>(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }

        public Optional<T> getData() {
            return Optional.ofNullable(data);
        }

        public Optional<Exception> getError() {
            return Optional.ofNullable(error);
        }
    }

    /**
     * Custom HTTP exception
     */
    public static class HttpException extends Exception {
        private final int code;

        public HttpException(int code, String message) {
            super(message);
            this.code = code;
        }

        public HttpException(String message) {
            super(message);
            this.code = -1;
        }

        public HttpException(String message, Throwable cause) {
            super(message, cause);
            this.code = -1;
        }

        public int getCode() {
            return code;
        }
    }
}
```

### 3. Request/Response Logging

```java
import okhttp3.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetailedLoggingInterceptor implements Interceptor {

    private static final Logger logger =
            Logger.getLogger(DetailedLoggingInterceptor.class.getName());

    private final Level level;
    private final boolean logBody;

    public DetailedLoggingInterceptor(Level level, boolean logBody) {
        this.level = level;
        this.logBody = logBody;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();

        // Log request
        logRequest(request);

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log(level, "<-- HTTP FAILED: " + e.getMessage(), e);
            throw e;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to ms

        // Log response
        logResponse(response, duration);

        return response;
    }

    private void logRequest(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("--> ").append(request.method())
          .append(' ').append(request.url())
          .append('\n');

        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            sb.append(headers.name(i)).append(": ")
              .append(headers.value(i)).append('\n');
        }

        if (logBody && request.body() != null) {
            sb.append("\nRequest Body: ")
              .append(bodyToString(request))
              .append('\n');
        }

        sb.append("--> END ").append(request.method());

        logger.log(level, sb.toString());
    }

    private void logResponse(Response response, long duration) {
        StringBuilder sb = new StringBuilder();
        sb.append("<-- ").append(response.code())
          .append(' ').append(response.message())
          .append(' ').append(response.request().url())
          .append(" (").append(duration).append("ms)\n");

        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            sb.append(headers.name(i)).append(": ")
              .append(headers.value(i)).append('\n');
        }

        if (logBody && response.body() != null) {
            try {
                String body = response.peekBody(1024 * 1024).string();
                sb.append("\nResponse Body: ").append(body).append('\n');
            } catch (IOException e) {
                sb.append("\nResponse Body: (error reading body)\n");
            }
        }

        sb.append("<-- END HTTP");

        logger.log(level, sb.toString());
    }

    private String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            okio.Buffer buffer = new okio.Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return "(error reading body)";
        }
    }
}
```

### 4. Rate Limiting

```java
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimitInterceptor implements Interceptor {

    private final Semaphore semaphore;
    private final long permits;
    private final long period;
    private final TimeUnit unit;

    /**
     * @param permits Maximum number of requests
     * @param period Time period
     * @param unit Time unit
     */
    public RateLimitInterceptor(long permits, long period, TimeUnit unit) {
        this.permits = permits;
        this.period = period;
        this.unit = unit;
        this.semaphore = new Semaphore((int) permits);

        // Reset permits periodically
        startPermitRefresh();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            semaphore.acquire();
            return chain.proceed(chain.request());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Rate limit acquisition interrupted", e);
        }
    }

    private void startPermitRefresh() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    unit.sleep(period);
                    // Release all permits up to the limit
                    int released = semaphore.drainPermits();
                    semaphore.release((int) permits);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "RateLimitRefresh").start();
    }
}
```

## Testing

### 1. Unit Testing with MockWebServer

```java
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class OkHttpClientTest {

    private MockWebServer server;
    private OkHttpClient client;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        client = new OkHttpClient.Builder()
                .build();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testSuccessfulGetRequest() throws Exception {
        // Prepare mock response
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"success\"}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        // Make request
        Request request = new Request.Builder()
                .url(server.url("/api/test"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Verify response
            assertTrue(response.isSuccessful());
            assertEquals(200, response.code());
            assertEquals("{\"status\":\"success\"}",
                        response.body().string());
        }

        // Verify request
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/test", recordedRequest.getPath());
    }

    @Test
    public void testPostRequestWithBody() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody("{\"id\":\"123\"}"));

        String jsonBody = "{\"name\":\"test\"}";
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(server.url("/api/create"))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(201, response.code());
        }

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(jsonBody, recordedRequest.getBody().readUtf8());
        assertEquals("application/json; charset=utf-8",
                    recordedRequest.getHeader("Content-Type"));
    }

    @Test
    public void testRetryOnFailure() throws Exception {
        // First request fails
        server.enqueue(new MockResponse()
                .setResponseCode(500));

        // Second request succeeds
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("success"));

        OkHttpClient retryClient = client.newBuilder()
                .addInterceptor(new RetryInterceptor(2))
                .build();

        Request request = new Request.Builder()
                .url(server.url("/api/test"))
                .build();

        try (Response response = retryClient.newCall(request).execute()) {
            assertEquals(200, response.code());
        }

        // Verify both requests were made
        assertEquals(2, server.getRequestCount());
    }

    @Test
    public void testTimeout() {
        server.enqueue(new MockResponse()
                .setBody("delayed response")
                .setBodyDelay(5, java.util.concurrent.TimeUnit.SECONDS));

        OkHttpClient timeoutClient = client.newBuilder()
                .readTimeout(java.time.Duration.ofSeconds(1))
                .build();

        Request request = new Request.Builder()
                .url(server.url("/api/slow"))
                .build();

        assertThrows(java.net.SocketTimeoutException.class, () -> {
            timeoutClient.newCall(request).execute();
        });
    }

    @Test
    public void testCustomHeaders() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200));

        Request request = new Request.Builder()
                .url(server.url("/api/test"))
                .header("Authorization", "Bearer token123")
                .header("Custom-Header", "custom-value")
                .build();

        client.newCall(request).execute().close();

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("Bearer token123",
                    recordedRequest.getHeader("Authorization"));
        assertEquals("custom-value",
                    recordedRequest.getHeader("Custom-Header"));
    }
}
```

### 2. Integration Testing

```java
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class IntegrationTest {

    private OkHttpClient client;

    @Before
    public void setUp() {
        client = OkHttpClientFactory.getInstance();
    }

    @Test
    public void testRealApiEndpoint() throws IOException {
        // Skip if not running integration tests
        String skipIntegration = System.getProperty("skip.integration");
        if ("true".equals(skipIntegration)) {
            return;
        }

        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.isSuccessful());
            assertNotNull(response.body());
        }
    }

    @Test
    public void testAsyncRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Response> responseRef = new AtomicReference<>();
        AtomicReference<IOException> errorRef = new AtomicReference<>();

        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorRef.set(e);
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) {
                responseRef.set(response);
                latch.countDown();
            }
        });

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        assertNull(errorRef.get());
        assertNotNull(responseRef.get());
        assertTrue(responseRef.get().isSuccessful());

        responseRef.get().close();
    }
}
```

## Performance Optimization

### 1. Response Caching

```java
import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class CachingConfiguration {

    /**
     * Create client with disk cache
     */
    public static OkHttpClient createCachedClient() {
        int cacheSize = 50 * 1024 * 1024; // 50 MB
        File cacheDirectory = new File(
                System.getProperty("java.io.tmpdir"),
                "okhttp-cache"
        );
        Cache cache = new Cache(cacheDirectory, cacheSize);

        return new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CacheControlInterceptor())
                .build();
    }

    /**
     * Cache control interceptor
     */
    static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // Force cache for GET requests
            if ("GET".equals(request.method())) {
                request = request.newBuilder()
                        .cacheControl(new CacheControl.Builder()
                                .maxAge(5, java.util.concurrent.TimeUnit.MINUTES)
                                .build())
                        .build();
            }

            Response response = chain.proceed(request);

            // Customize cache for responses
            return response.newBuilder()
                    .header("Cache-Control",
                           "public, max-age=300") // 5 minutes
                    .removeHeader("Pragma")
                    .build();
        }
    }

    /**
     * Force network for specific request
     */
    public static Request forceNetworkRequest(String url) {
        return new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder()
                        .noCache()
                        .build())
                .build();
    }

    /**
     * Force cache for specific request
     */
    public static Request forceCacheRequest(String url) {
        return new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(7, java.util.concurrent.TimeUnit.DAYS)
                        .build())
                .build();
    }
}
```

### 2. Connection Pooling Metrics

```java
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class ConnectionPoolMonitor implements ConnectionPoolMonitorMBean {

    private final ConnectionPool pool;

    public ConnectionPoolMonitor(ConnectionPool pool) {
        this.pool = pool;
        registerMBean();
    }

    @Override
    public int getIdleConnectionCount() {
        return pool.idleConnectionCount();
    }

    @Override
    public int getConnectionCount() {
        return pool.connectionCount();
    }

    private void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName(
                    "com.example:type=ConnectionPool,name=OkHttp"
            );
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            System.err.println("Failed to register MBean: " + e.getMessage());
        }
    }

    public interface ConnectionPoolMonitorMBean {
        int getIdleConnectionCount();
        int getConnectionCount();
    }
}
```

### 3. Request Queue Management

```java
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestQueueManager {

    private final OkHttpClient client;
    private final ExecutorService executor;
    private final Semaphore semaphore;
    private final AtomicInteger activeRequests;

    public RequestQueueManager(int maxConcurrentRequests) {
        this.client = OkHttpClientFactory.getInstance();
        this.executor = Executors.newFixedThreadPool(maxConcurrentRequests);
        this.semaphore = new Semaphore(maxConcurrentRequests);
        this.activeRequests = new AtomicInteger(0);
    }

    /**
     * Execute request with controlled concurrency
     */
    public CompletableFuture<Response> enqueue(Request request) {
        CompletableFuture<Response> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                semaphore.acquire();
                activeRequests.incrementAndGet();

                Response response = client.newCall(request).execute();
                future.complete(response);

            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                activeRequests.decrementAndGet();
                semaphore.release();
            }
        });

        return future;
    }

    public int getActiveRequests() {
        return activeRequests.get();
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

## Complete Production Example

```java
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Production-ready HTTP client service
 */
public class HttpClientService {

    private final OkHttpClient client;
    private final ResilientHttpClient resilientClient;

    public HttpClientService(Config config) {
        this.client = buildClient(config);
        this.resilientClient = new ResilientHttpClient();
    }

    private OkHttpClient buildClient(Config config) {
        ConnectionPool connectionPool = new ConnectionPool(
                config.maxIdleConnections,
                config.keepAliveDuration,
                TimeUnit.MINUTES
        );

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(config.logLevel);

        return new OkHttpClient.Builder()
                // Timeouts
                .connectTimeout(Duration.ofSeconds(config.connectTimeout))
                .readTimeout(Duration.ofSeconds(config.readTimeout))
                .writeTimeout(Duration.ofSeconds(config.writeTimeout))
                .callTimeout(Duration.ofSeconds(config.callTimeout))

                // Connection pool
                .connectionPool(connectionPool)

                // Interceptors
                .addInterceptor(new RetryInterceptor(config.maxRetries))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new RateLimitInterceptor(
                        config.rateLimit,
                        1,
                        TimeUnit.SECONDS
                ))

                // Cache
                .cache(new Cache(
                        config.cacheDirectory,
                        config.cacheSize
                ))

                // Protocols
                .protocols(java.util.List.of(
                        Protocol.HTTP_2,
                        Protocol.HTTP_1_1
                ))

                // Redirects and retries
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)

                .build();
    }

    /**
     * Synchronous GET request
     */
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * Asynchronous GET request
     */
    public CompletableFuture<String> getAsync(String url) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(
                                new IOException("Unexpected code " + response)
                        );
                        return;
                    }
                    future.complete(body.string());
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * POST JSON
     */
    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * Shutdown client and clean up resources
     */
    public void shutdown() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();

        Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
                System.err.println("Error closing cache: " + e.getMessage());
            }
        }
    }

    /**
     * Configuration class
     */
    public static class Config {
        int connectTimeout = 30;
        int readTimeout = 30;
        int writeTimeout = 30;
        int callTimeout = 90;
        int maxIdleConnections = 5;
        int keepAliveDuration = 5;
        int maxRetries = 3;
        int rateLimit = 100;
        long cacheSize = 10 * 1024 * 1024; // 10 MB
        java.io.File cacheDirectory = new java.io.File(
                System.getProperty("java.io.tmpdir"),
                "okhttp-cache"
        );
        HttpLoggingInterceptor.Level logLevel =
                HttpLoggingInterceptor.Level.BASIC;

        public Config() {}

        // Builder methods
        public Config connectTimeout(int seconds) {
            this.connectTimeout = seconds;
            return this;
        }

        public Config readTimeout(int seconds) {
            this.readTimeout = seconds;
            return this;
        }

        public Config maxRetries(int retries) {
            this.maxRetries = retries;
            return this;
        }

        public Config logLevel(HttpLoggingInterceptor.Level level) {
            this.logLevel = level;
            return this;
        }
    }
}
```

### Usage Example

```java
public class Main {
    public static void main(String[] args) {
        // Configure and create service
        HttpClientService.Config config = new HttpClientService.Config()
                .connectTimeout(15)
                .readTimeout(30)
                .maxRetries(3)
                .logLevel(HttpLoggingInterceptor.Level.BASIC);

        HttpClientService httpService = new HttpClientService(config);

        try {
            // Synchronous request
            String response = httpService.get("https://api.example.com/data");
            System.out.println("Response: " + response);

            // Asynchronous request
            httpService.getAsync("https://api.example.com/data")
                    .thenAccept(result -> System.out.println("Async: " + result))
                    .exceptionally(e -> {
                        System.err.println("Error: " + e.getMessage());
                        return null;
                    });

            // POST request
            String jsonData = "{\"key\":\"value\"}";
            String postResponse = httpService.postJson(
                    "https://api.example.com/create",
                    jsonData
            );
            System.out.println("POST Response: " + postResponse);

        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        } finally {
            // Cleanup
            httpService.shutdown();
        }
    }
}
```

This comprehensive guide covers all essential aspects of using OkHttp 5.x in production environments, including configuration, error handling, testing, performance optimization, and best practices. The code examples are production-ready and follow industry standards.
