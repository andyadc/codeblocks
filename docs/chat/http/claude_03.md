# Comprehensive Guide to Apache HttpClient 4.x for Production

## Table of Contents
1. [Introduction](#introduction)
2. [Setup and Dependencies](#setup)
3. [Basic Usage](#basic-usage)
4. [Connection Management](#connection-management)
5. [Error Handling and Retries](#error-handling)
6. [Security and SSL/TLS](#security)
7. [Authentication](#authentication)
8. [Performance Optimization](#performance)
9. [Monitoring and Logging](#monitoring)
10. [Production-Ready Examples](#production-examples)

## <a name="introduction"></a>1. Introduction

Apache HttpClient 4.x is a robust, feature-rich HTTP client library for Java applications. This guide covers production-grade implementations with best practices.

## <a name="setup"></a>2. Setup and Dependencies

### Maven Dependencies

```xml
<dependencies>
    <!-- Apache HttpClient -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>

    <!-- Connection pooling -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpcore</artifactId>
        <version>4.4.16</version>
    </dependency>

    <!-- JSON processing (optional) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.36</version>
    </dependency>
</dependencies>
```

### Gradle Dependencies

```gradle
dependencies {
    implementation 'org.apache.httpcomponents:httpclient:4.5.14'
    implementation 'org.apache.httpcomponents:httpcore:4.4.16'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
    implementation 'org.slf4j:slf4j-api:1.7.36'
}
```

## <a name="basic-usage"></a>3. Basic Usage

### Simple GET Request

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SimpleHttpClientExample {

    public static void main(String[] args) {
        // Create a client (not recommended for production - use pooling)
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet("https://api.example.com/users");

            // Add headers
            request.addHeader("Accept", "application/json");
            request.addHeader("User-Agent", "MyApp/1.0");

            // Execute request
            HttpResponse response = httpClient.execute(request);

            // Get status code
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code: " + statusCode);

            // Get response body
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### POST Request with JSON

```java
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PostRequestExample {

    public static void createUser(String jsonPayload) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpPost post = new HttpPost("https://api.example.com/users");

            // Set headers
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "application/json");

            // Set request body
            StringEntity entity = new StringEntity(jsonPayload, "UTF-8");
            post.setEntity(entity);

            // Execute and get response
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                System.out.println("Status: " + statusCode);
                System.out.println("Response: " + responseBody);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";
        createUser(json);
    }
}
```

## <a name="connection-management"></a>4. Connection Management

### Production-Grade Connection Pool Configuration

```java
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;

import java.util.concurrent.TimeUnit;

public class HttpClientFactory {

    private static final int MAX_TOTAL_CONNECTIONS = 200;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 20;
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds
    private static final int SOCKET_TIMEOUT = 30000; // 30 seconds
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000; // 5 seconds
    private static final int DEFAULT_KEEP_ALIVE_TIME = 20000; // 20 seconds

    public static CloseableHttpClient createHttpClient() {
        // Configure connection pool
        PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager();

        // Set maximum total connections
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

        // Set maximum connections per route (default)
        connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);

        // Set maximum connections for specific hosts
        HttpHost localhost = new HttpHost("localhost", 8080);
        connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);

        // Configure request timeouts
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .build();

        // Configure keep-alive strategy
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));

            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();

                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
            return DEFAULT_KEEP_ALIVE_TIME;
        };

        // Build the client
        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .setKeepAliveStrategy(keepAliveStrategy)
            .build();
    }
}
```

### Idle Connection Monitor

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitor extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);
    private final PoolingHttpClientConnectionManager connectionManager;
    private volatile boolean shutdown;

    public IdleConnectionMonitor(PoolingHttpClientConnectionManager connectionManager) {
        super("IdleConnectionMonitor");
        this.connectionManager = connectionManager;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000); // Check every 5 seconds

                    // Close expired connections
                    connectionManager.closeExpiredConnections();

                    // Close connections idle for more than 30 seconds
                    connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

                    logger.debug("Connection pool stats: {}",
                        connectionManager.getTotalStats());
                }
            }
        } catch (InterruptedException e) {
            logger.warn("Idle connection monitor interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
```

## <a name="error-handling"></a>5. Error Handling and Retries

### Retry Handler Implementation

```java
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

public class CustomRetryHandler implements HttpRequestRetryHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomRetryHandler.class);
    private static final int MAX_RETRIES = 3;

    @Override
    public boolean retryRequest(IOException exception, int executionCount,
                                HttpContext context) {

        logger.warn("Request failed (attempt {}): {}",
            executionCount, exception.getMessage());

        if (executionCount > MAX_RETRIES) {
            logger.error("Maximum retries ({}) exceeded", MAX_RETRIES);
            return false;
        }

        // Don't retry if timeout
        if (exception instanceof InterruptedIOException) {
            logger.debug("Request timed out");
            return false;
        }

        // Don't retry on unknown host
        if (exception instanceof UnknownHostException) {
            logger.error("Unknown host");
            return false;
        }

        // Don't retry on connection timeout
        if (exception instanceof ConnectTimeoutException) {
            logger.error("Connection timeout");
            return false;
        }

        // Don't retry on SSL errors
        if (exception instanceof SSLException) {
            logger.error("SSL handshake exception");
            return false;
        }

        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();

        // Retry if the request is idempotent
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);

        if (idempotent) {
            logger.info("Retrying idempotent request");
            return true;
        }

        return false;
    }
}
```

### Service Unavailable Retry Strategy

```java
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomServiceUnavailableRetryStrategy
    implements ServiceUnavailableRetryStrategy {

    private static final Logger logger =
        LoggerFactory.getLogger(CustomServiceUnavailableRetryStrategy.class);

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL = 2000; // 2 seconds

    @Override
    public boolean retryRequest(HttpResponse response, int executionCount,
                                HttpContext context) {

        int statusCode = response.getStatusLine().getStatusCode();

        logger.warn("Received status code {} (attempt {})",
            statusCode, executionCount);

        // Retry on 503 Service Unavailable or 429 Too Many Requests
        boolean shouldRetry = executionCount <= MAX_RETRIES &&
            (statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE ||
             statusCode == 429);

        if (shouldRetry) {
            logger.info("Will retry request");
        }

        return shouldRetry;
    }

    @Override
    public long getRetryInterval() {
        return RETRY_INTERVAL;
    }
}
```

### Comprehensive Error Handling Example

```java
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class RobustHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(RobustHttpClient.class);
    private final CloseableHttpClient httpClient;

    public RobustHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String executeGetRequest(String url) throws HttpClientException {
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            if (statusCode >= 200 && statusCode < 300) {
                logger.debug("Request successful: {}", url);
                return responseBody;
            } else if (statusCode >= 400 && statusCode < 500) {
                logger.error("Client error {}: {}", statusCode, url);
                throw new HttpClientException(
                    "Client error: " + statusCode, statusCode, responseBody);
            } else if (statusCode >= 500) {
                logger.error("Server error {}: {}", statusCode, url);
                throw new HttpClientException(
                    "Server error: " + statusCode, statusCode, responseBody);
            } else {
                throw new HttpClientException(
                    "Unexpected status: " + statusCode, statusCode, responseBody);
            }

        } catch (SocketTimeoutException e) {
            logger.error("Request timeout: {}", url, e);
            throw new HttpClientException("Request timeout", e);
        } catch (UnknownHostException e) {
            logger.error("Unknown host: {}", url, e);
            throw new HttpClientException("Unknown host", e);
        } catch (IOException e) {
            logger.error("IO error executing request: {}", url, e);
            throw new HttpClientException("IO error", e);
        }
    }

    public static class HttpClientException extends Exception {
        private final Integer statusCode;
        private final String responseBody;

        public HttpClientException(String message, Throwable cause) {
            super(message, cause);
            this.statusCode = null;
            this.responseBody = null;
        }

        public HttpClientException(String message, int statusCode, String responseBody) {
            super(message);
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }
    }
}
```

## <a name="security"></a>6. Security and SSL/TLS

### Custom SSL Context Configuration

```java
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SecureHttpClientFactory {

    /**
     * Create HTTP client with custom truststore
     */
    public static CloseableHttpClient createWithCustomTruststore(
            String truststorePath, String truststorePassword) throws Exception {

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(new File(truststorePath))) {
            trustStore.load(instream, truststorePassword.toCharArray());
        }

        SSLContext sslContext = SSLContexts.custom()
            .loadTrustMaterial(trustStore, null)
            .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslContext,
            new String[]{"TLSv1.2", "TLSv1.3"}, // Supported protocols
            null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .build();
    }

    /**
     * Create HTTP client with client certificate authentication
     */
    public static CloseableHttpClient createWithClientCertificate(
            String keystorePath, String keystorePassword,
            String truststorePath, String truststorePassword) throws Exception {

        // Load keystore with client certificate
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream instream = new FileInputStream(new File(keystorePath))) {
            keyStore.load(instream, keystorePassword.toCharArray());
        }

        // Load truststore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(new File(truststorePath))) {
            trustStore.load(instream, truststorePassword.toCharArray());
        }

        SSLContext sslContext = SSLContextBuilder.create()
            .loadKeyMaterial(keyStore, keystorePassword.toCharArray())
            .loadTrustMaterial(trustStore, null)
            .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslContext,
            new String[]{"TLSv1.2", "TLSv1.3"},
            null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .build();
    }

    /**
     * Create HTTP client that accepts self-signed certificates
     * WARNING: Use only for development/testing!
     */
    public static CloseableHttpClient createInsecureClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(new TrustSelfSignedStrategy())
            .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslContext,
            NoopHostnameVerifier.INSTANCE);

        return HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .build();
    }
}
```

## <a name="authentication"></a>7. Authentication

### Basic Authentication

```java
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BasicAuthExample {

    public static String executeWithBasicAuth(String url,
                                              String username,
                                              String password) throws Exception {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            AuthScope.ANY,
            new UsernamePasswordCredentials(username, password));

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build()) {

            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}
```

### Bearer Token Authentication

```java
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BearerTokenAuth {

    public static String executeWithBearerToken(String url, String token)
            throws Exception {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "Bearer " + token);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}
```

### OAuth 2.0 Client Credentials Flow

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class OAuth2ClientCredentials {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;

    private String accessToken;
    private long tokenExpiryTime;

    public OAuth2ClientCredentials(CloseableHttpClient httpClient,
                                   String tokenEndpoint,
                                   String clientId,
                                   String clientSecret) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.tokenEndpoint = tokenEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public synchronized String getAccessToken() throws Exception {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiryTime) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private void refreshAccessToken() throws Exception {
        HttpPost post = new HttpPost(tokenEndpoint);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode json = objectMapper.readTree(responseBody);

            this.accessToken = json.get("access_token").asText();
            int expiresIn = json.get("expires_in").asInt();

            // Refresh 5 minutes before actual expiry
            this.tokenExpiryTime = System.currentTimeMillis() +
                ((expiresIn - 300) * 1000L);
        }
    }
}
```

## <a name="performance"></a>8. Performance Optimization

### Async Request Execution

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class AsyncHttpClientExample {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientExample.class);

    public static CloseableHttpAsyncClient createAsyncClient() throws Exception {
        IOReactorConfig ioConfig = IOReactorConfig.custom()
            .setConnectTimeout(5000)
            .setSoTimeout(30000)
            .setIoThreadCount(Runtime.getRuntime().availableProcessors())
            .build();

        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioConfig);
        PoolingNHttpClientConnectionManager connectionManager =
            new PoolingNHttpClientConnectionManager(ioReactor);

        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        return HttpAsyncClients.custom()
            .setConnectionManager(connectionManager)
            .build();
    }

    public static CompletableFuture<String> executeAsync(
            CloseableHttpAsyncClient httpClient, String url) {

        CompletableFuture<String> future = new CompletableFuture<>();
        HttpGet request = new HttpGet(url);

        httpClient.execute(request, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                try {
                    String result = EntityUtils.toString(response.getEntity());
                    future.complete(result);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("Request failed: {}", url, ex);
                future.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                logger.warn("Request cancelled: {}", url);
                future.cancel(true);
            }
        });

        return future;
    }

    public static void main(String[] args) throws Exception {
        CloseableHttpAsyncClient httpClient = createAsyncClient();
        httpClient.start();

        try {
            CompletableFuture<String> future = executeAsync(
                httpClient, "https://api.example.com/data");

            String result = future.get(); // Block until complete
            System.out.println("Result: " + result);

        } finally {
            httpClient.close();
        }
    }
}
```

### Request Compression

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CompressionExample {

    public static String executeWithCompression(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .build()) {

            HttpGet request = new HttpGet(url);

            // Request compressed response
            request.addHeader("Accept-Encoding", "gzip, deflate");

            HttpResponse response = httpClient.execute(request);

            // HttpClient automatically decompresses gzip responses
            return EntityUtils.toString(response.getEntity());
        }
    }
}
```

### Response Streaming for Large Files

```java
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.FileOutputStream;
import java.io.InputStream;

public class StreamingDownload {

    public static void downloadLargeFile(CloseableHttpClient httpClient,
                                        String url,
                                        String outputPath) throws Exception {

        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream inputStream = entity.getContent();
                     FileOutputStream outputStream = new FileOutputStream(outputPath)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }
}
```

## <a name="monitoring"></a>9. Monitoring and Logging

### Request/Response Logging Interceptor

```java
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoggingInterceptors {

    public static class RequestLoggingInterceptor implements HttpRequestInterceptor {

        private static final Logger logger = LoggerFactory.getLogger("HTTP_REQUEST");

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {

            logger.info(">>> Request: {} {}",
                request.getRequestLine().getMethod(),
                request.getRequestLine().getUri());

            for (Header header : request.getAllHeaders()) {
                logger.debug(">>> Header: {}: {}", header.getName(), header.getValue());
            }
        }
    }

    public static class ResponseLoggingInterceptor implements HttpResponseInterceptor {

        private static final Logger logger = LoggerFactory.getLogger("HTTP_RESPONSE");

        @Override
        public void process(HttpResponse response, HttpContext context)
                throws HttpException, IOException {

            logger.info("<<< Response: {}",
                response.getStatusLine());

            for (Header header : response.getAllHeaders()) {
                logger.debug("<<< Header: {}: {}", header.getName(), header.getValue());
            }
        }
    }
}
```

### Metrics Collection

```java
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolMetrics {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolMetrics.class);
    private final PoolingHttpClientConnectionManager connectionManager;
    private final ScheduledExecutorService scheduler;

    public ConnectionPoolMetrics(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::logMetrics, 0, 30, TimeUnit.SECONDS);
    }

    public void stopMonitoring() {
        scheduler.shutdown();
    }

    private void logMetrics() {
        PoolStats totalStats = connectionManager.getTotalStats();

        logger.info("=== Connection Pool Stats ===");
        logger.info("Total: Available={}, Leased={}, Pending={}, Max={}",
            totalStats.getAvailable(),
            totalStats.getLeased(),
            totalStats.getPending(),
            totalStats.getMax());

        // Log per-route stats if needed
        for (HttpRoute route : connectionManager.getRoutes()) {
            PoolStats routeStats = connectionManager.getStats(route);
            logger.debug("Route {}: Available={}, Leased={}, Pending={}",
                route,
                routeStats.getAvailable(),
                routeStats.getLeased(),
                routeStats.getPending());
        }
    }
}
```

## <a name="production-examples"></a>10. Production-Ready Examples

### Complete Production HTTP Client Service

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Production-ready HTTP client service with comprehensive error handling,
 * connection pooling, retry logic, and monitoring.
 */
public class ProductionHttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionHttpClientService.class);

    private final CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final IdleConnectionMonitor idleConnectionMonitor;
    private final ObjectMapper objectMapper;
    private final ConnectionPoolMetrics metrics;

    public ProductionHttpClientService() {
        this.objectMapper = new ObjectMapper();
        this.connectionManager = createConnectionManager();
        this.httpClient = createHttpClient(connectionManager);
        this.idleConnectionMonitor = new IdleConnectionMonitor(connectionManager);
        this.idleConnectionMonitor.start();
        this.metrics = new ConnectionPoolMetrics(connectionManager);
        this.metrics.startMonitoring();

        logger.info("ProductionHttpClientService initialized");
    }

    private PoolingHttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        cm.setValidateAfterInactivity(2000);
        return cm;
    }

    private CloseableHttpClient createHttpClient(PoolingHttpClientConnectionManager cm) {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(30000)
            .setConnectionRequestTimeout(5000)
            .build();

        return HttpClients.custom()
            .setConnectionManager(cm)
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(new CustomRetryHandler())
            .setServiceUnavailableRetryStrategy(new CustomServiceUnavailableRetryStrategy())
            .addInterceptorFirst(new LoggingInterceptors.RequestLoggingInterceptor())
            .addInterceptorLast(new LoggingInterceptors.ResponseLoggingInterceptor())
            .build();
    }

    /**
     * Execute GET request
     */
    public <T> ApiResponse<T> get(String url, Class<T> responseType) {
        return get(url, null, responseType);
    }

    /**
     * Execute GET request with headers
     */
    public <T> ApiResponse<T> get(String url, Map<String, String> headers,
                                   Class<T> responseType) {
        HttpGet request = new HttpGet(url);
        addHeaders(request, headers);
        return executeRequest(request, responseType);
    }

    /**
     * Execute POST request with JSON body
     */
    public <T> ApiResponse<T> post(String url, Object requestBody,
                                    Class<T> responseType) {
        return post(url, requestBody, null, responseType);
    }

    /**
     * Execute POST request with JSON body and headers
     */
    public <T> ApiResponse<T> post(String url, Object requestBody,
                                    Map<String, String> headers,
                                    Class<T> responseType) {
        try {
            HttpPost request = new HttpPost(url);

            if (requestBody != null) {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                request.setEntity(entity);
            }

            addHeaders(request, headers);
            return executeRequest(request, responseType);

        } catch (Exception e) {
            logger.error("Error creating POST request", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Execute PUT request
     */
    public <T> ApiResponse<T> put(String url, Object requestBody,
                                   Class<T> responseType) {
        return put(url, requestBody, null, responseType);
    }

    /**
     * Execute PUT request with headers
     */
    public <T> ApiResponse<T> put(String url, Object requestBody,
                                   Map<String, String> headers,
                                   Class<T> responseType) {
        try {
            HttpPut request = new HttpPut(url);

            if (requestBody != null) {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                request.setEntity(entity);
            }

            addHeaders(request, headers);
            return executeRequest(request, responseType);

        } catch (Exception e) {
            logger.error("Error creating PUT request", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Execute DELETE request
     */
    public <T> ApiResponse<T> delete(String url, Class<T> responseType) {
        return delete(url, null, responseType);
    }

    /**
     * Execute DELETE request with headers
     */
    public <T> ApiResponse<T> delete(String url, Map<String, String> headers,
                                      Class<T> responseType) {
        HttpDelete request = new HttpDelete(url);
        addHeaders(request, headers);
        return executeRequest(request, responseType);
    }

    /**
     * Execute HTTP request and parse response
     */
    private <T> ApiResponse<T> executeRequest(HttpUriRequest request,
                                               Class<T> responseType) {
        long startTime = System.currentTimeMillis();

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            long duration = System.currentTimeMillis() - startTime;

            logger.debug("Request completed in {}ms with status {}", duration, statusCode);

            if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                T data = parseResponse(responseBody, responseType);
                return ApiResponse.success(data, statusCode);
            } else {
                logger.warn("Request failed with status {}: {}", statusCode, responseBody);
                return ApiResponse.error(statusCode, responseBody);
            }

        } catch (IOException e) {
            logger.error("Error executing request", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * Parse JSON response to object
     */
    private <T> T parseResponse(String responseBody, Class<T> responseType) {
        if (responseType == String.class) {
            return (T) responseBody;
        }

        if (responseType == Void.class) {
            return null;
        }

        try {
            return objectMapper.readValue(responseBody, responseType);
        } catch (Exception e) {
            logger.error("Error parsing response", e);
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    /**
     * Add headers to request
     */
    private void addHeaders(HttpRequest request, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(request::addHeader);
        }
    }

    /**
     * Cleanup resources
     */
    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down ProductionHttpClientService");

        try {
            metrics.stopMonitoring();
            idleConnectionMonitor.shutdown();
            httpClient.close();
            connectionManager.close();
            logger.info("ProductionHttpClientService shutdown complete");
        } catch (Exception e) {
            logger.error("Error during shutdown", e);
        }
    }

    /**
     * Get connection pool statistics
     */
    public String getPoolStats() {
        return connectionManager.getTotalStats().toString();
    }

    /**
     * API Response wrapper
     */
    public static class ApiResponse<T> {
        private final boolean success;
        private final T data;
        private final Integer statusCode;
        private final String errorMessage;

        private ApiResponse(boolean success, T data, Integer statusCode, String errorMessage) {
            this.success = success;
            this.data = data;
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
        }

        public static <T> ApiResponse<T> success(T data, int statusCode) {
            return new ApiResponse<>(true, data, statusCode, null);
        }

        public static <T> ApiResponse<T> error(int statusCode, String errorMessage) {
            return new ApiResponse<>(false, null, statusCode, errorMessage);
        }

        public static <T> ApiResponse<T> error(String errorMessage) {
            return new ApiResponse<>(false, null, null, errorMessage);
        }

        public boolean isSuccess() {
            return success;
        }

        public T getData() {
            return data;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
```

### Usage Example of Production Service

```java
import java.util.HashMap;
import java.util.Map;

public class ProductionServiceExample {

    public static void main(String[] args) {
        ProductionHttpClientService httpService = new ProductionHttpClientService();

        try {
            // Example 1: Simple GET request
            ProductionHttpClientService.ApiResponse<User> userResponse =
                httpService.get("https://api.example.com/users/123", User.class);

            if (userResponse.isSuccess()) {
                User user = userResponse.getData();
                System.out.println("User: " + user.getName());
            } else {
                System.err.println("Error: " + userResponse.getErrorMessage());
            }

            // Example 2: POST request with headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer your-token-here");
            headers.put("X-Request-ID", "12345");

            CreateUserRequest newUser = new CreateUserRequest("John Doe", "john@example.com");
            ProductionHttpClientService.ApiResponse<User> createResponse =
                httpService.post("https://api.example.com/users", newUser, headers, User.class);

            if (createResponse.isSuccess()) {
                System.out.println("User created with status: " + createResponse.getStatusCode());
            }

            // Example 3: PUT request
            UpdateUserRequest updateData = new UpdateUserRequest("Jane Doe");
            ProductionHttpClientService.ApiResponse<User> updateResponse =
                httpService.put("https://api.example.com/users/123", updateData, User.class);

            // Example 4: DELETE request
            ProductionHttpClientService.ApiResponse<Void> deleteResponse =
                httpService.delete("https://api.example.com/users/123", Void.class);

            // Check pool statistics
            System.out.println("Pool Stats: " + httpService.getPoolStats());

        } finally {
            httpService.shutdown();
        }
    }

    static class User {
        private String id;
        private String name;
        private String email;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    static class CreateUserRequest {
        private String name;
        private String email;

        public CreateUserRequest(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Getters
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    static class UpdateUserRequest {
        private String name;

        public UpdateUserRequest(String name) {
            this.name = name;
        }

        public String getName() { return name; }
    }
}
```

### Spring Boot Integration

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class HttpClientConfig {

    @Value("${http.client.max.total:200}")
    private int maxTotal;

    @Value("${http.client.max.per.route:20}")
    private int maxPerRoute;

    @Value("${http.client.connect.timeout:5000}")
    private int connectTimeout;

    @Value("${http.client.socket.timeout:30000}")
    private int socketTimeout;

    @Bean
    public ProductionHttpClientService httpClientService() {
        return new ProductionHttpClientService();
    }
}
```

### application.properties

```properties
# HTTP Client Configuration
http.client.max.total=200
http.client.max.per.route=20
http.client.connect.timeout=5000
http.client.socket.timeout=30000
http.client.connection.request.timeout=5000

# Logging
logging.level.HTTP_REQUEST=INFO
logging.level.HTTP_RESPONSE=INFO
logging.level.com.yourpackage.httpclient=DEBUG
```

## Best Practices Summary

1. **Always use connection pooling** - Never create a new HttpClient for each request
2. **Set appropriate timeouts** - Prevent hanging requests
3. **Implement retry logic** - Handle transient failures gracefully
4. **Use proper error handling** - Catch and handle specific exceptions
5. **Close resources properly** - Use try-with-resources
6. **Monitor connection pools** - Track metrics and detect leaks
7. **Use SSL/TLS properly** - Validate certificates in production
8. **Implement request/response logging** - For debugging and auditing
9. **Handle authentication securely** - Don't hardcode credentials
10. **Test under load** - Verify configuration under production-like conditions

This comprehensive guide provides production-ready implementations for Apache HttpClient 4.x with proper error handling, connection management, security, and monitoring capabilities.
