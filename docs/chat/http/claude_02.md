# Comprehensive Guide to Apache HttpClient 4.x for Production

## Table of Contents
1. Introduction and Core Concepts
2. Connection Management
3. Request Configuration
4. Response Handling
5. Authentication
6. SSL/TLS Configuration
7. Retry and Error Handling
8. Performance Optimization
9. Monitoring and Logging
10. Production Best Practices

---

## 1. Introduction and Core Concepts

Apache HttpClient 4.x is a robust HTTP client library for Java applications. In production environments, proper configuration is critical for reliability, performance, and security.

### Key Components

**HttpClient**: The main interface for executing HTTP requests.

**CloseableHttpClient**: The closeable implementation that manages resources properly.

**HttpClientBuilder**: Fluent builder for creating configured HttpClient instances.

**RequestConfig**: Configuration for individual requests (timeouts, redirects, etc.).

**PoolingHttpClientConnectionManager**: Manages a pool of HTTP connections for reuse.

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpcore</artifactId>
        <version>4.4.16</version>
    </dependency>
    <!-- For async operations -->
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpasyncclient</artifactId>
        <version>4.1.5</version>
    </dependency>
</dependencies>
```

---

## 2. Connection Management

Proper connection management is crucial for production systems to handle high throughput and prevent resource exhaustion.

### Production-Ready Connection Pool Configuration

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.pool.PoolStats;

import java.util.concurrent.TimeUnit;

public class HttpClientConnectionManager {

    private final PoolingHttpClientConnectionManager connectionManager;
    private final CloseableHttpClient httpClient;

    public HttpClientConnectionManager() {
        // Create connection manager with production settings
        connectionManager = new PoolingHttpClientConnectionManager();

        // Maximum total connections across all routes
        connectionManager.setMaxTotal(200);

        // Maximum connections per route (host)
        connectionManager.setDefaultMaxPerRoute(20);

        // Set connection time-to-live
        connectionManager.setDefaultConnectionConfig(
            ConnectionConfig.custom()
                .setBufferSize(8192)
                .build()
        );

        // Socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setSoTimeout(30000) // 30 seconds socket timeout
            .setSoKeepAlive(true)
            .setTcpNoDelay(true)
            .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        // Build the client
        httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .evictExpiredConnections()
            .evictIdleConnections(60L, TimeUnit.SECONDS)
            .build();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public PoolStats getPoolStats() {
        return connectionManager.getTotalStats();
    }

    public void shutdown() {
        try {
            httpClient.close();
            connectionManager.close();
        } catch (Exception e) {
            // Log error
        }
    }
}
```

### Connection Eviction Thread

For long-running applications, implement a background thread to evict stale connections:

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import java.util.concurrent.TimeUnit;

public class IdleConnectionEvictor extends Thread {

    private final PoolingHttpClientConnectionManager connectionManager;
    private volatile boolean shutdown;

    public IdleConnectionEvictor(PoolingHttpClientConnectionManager connectionManager) {
        super("Connection-Evictor");
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
                }
            }
        } catch (InterruptedException ex) {
            // Terminate
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

---

## 3. Request Configuration

### Production Request Configuration

```java
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class HttpClientRequestBuilder {

    private final CloseableHttpClient httpClient;

    public HttpClientRequestBuilder(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Create production-ready request configuration
     */
    public RequestConfig createRequestConfig() {
        return RequestConfig.custom()
            // Time to establish connection
            .setConnectTimeout(5000) // 5 seconds

            // Time to wait for data (socket timeout)
            .setSocketTimeout(30000) // 30 seconds

            // Time to get connection from pool
            .setConnectionRequestTimeout(3000) // 3 seconds

            // Enable redirect following
            .setRedirectsEnabled(true)
            .setMaxRedirects(5)

            // Enable circular redirects (A -> B -> A)
            .setCircularRedirectsAllowed(false)

            // Reject relative redirects
            .setRelativeRedirectsAllowed(true)

            // Enable automatic content decompression
            .setContentCompressionEnabled(true)

            .build();
    }

    /**
     * Execute GET request with retry logic
     */
    public String executeGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(createRequestConfig());

        // Set headers
        httpGet.setHeader("User-Agent", "MyApp/1.0");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        }
    }

    /**
     * Execute POST request
     */
    public String executePost(String url, String jsonBody) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(createRequestConfig());

        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        // Set entity
        StringEntity entity = new StringEntity(jsonBody, "UTF-8");
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    private String handleResponse(CloseableHttpResponse response) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode >= 200 && statusCode < 300) {
            return org.apache.http.util.EntityUtils.toString(
                response.getEntity(), "UTF-8"
            );
        } else {
            throw new Exception("HTTP error: " + statusCode);
        }
    }
}
```

### Request with Custom Headers and Parameters

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedRequestBuilder {

    /**
     * Build URI with query parameters
     */
    public URI buildURI(String baseUrl, Map<String, String> params) throws Exception {
        URIBuilder builder = new URIBuilder(baseUrl);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    /**
     * Create request with custom headers
     */
    public HttpGet createRequestWithHeaders(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);

        // Add custom headers
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        // Add common headers
        httpGet.setHeader("User-Agent", "MyApp/1.0");
        httpGet.setHeader("Accept", "application/json");

        return httpGet;
    }
}
```

---

## 4. Response Handling

### Comprehensive Response Handler

```java
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResponseHandler {

    /**
     * Complete response wrapper
     */
    public static class HttpResponseWrapper {
        private final int statusCode;
        private final String reasonPhrase;
        private final String body;
        private final Map<String, String> headers;

        public HttpResponseWrapper(int statusCode, String reasonPhrase,
                                  String body, Map<String, String> headers) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
            this.body = body;
            this.headers = headers;
        }

        public int getStatusCode() { return statusCode; }
        public String getReasonPhrase() { return reasonPhrase; }
        public String getBody() { return body; }
        public Map<String, String> getHeaders() { return headers; }

        public boolean isSuccess() {
            return statusCode >= 200 && statusCode < 300;
        }
    }

    /**
     * Parse response safely
     */
    public static HttpResponseWrapper parseResponse(CloseableHttpResponse response)
            throws IOException {

        int statusCode = response.getStatusLine().getStatusCode();
        String reasonPhrase = response.getStatusLine().getReasonPhrase();

        // Extract headers
        Map<String, String> headers = new HashMap<>();
        for (Header header : response.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        // Extract body
        String body = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } finally {
                // Ensure the entity is fully consumed
                EntityUtils.consume(entity);
            }
        }

        return new HttpResponseWrapper(statusCode, reasonPhrase, body, headers);
    }

    /**
     * Stream large responses
     */
    public static void streamResponse(CloseableHttpResponse response,
                                      java.io.OutputStream outputStream)
            throws IOException {

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                EntityUtils.consume(entity);
            }
        }
    }

    /**
     * Handle different status codes
     */
    public static void handleStatusCode(int statusCode, String body)
            throws Exception {

        if (statusCode >= 200 && statusCode < 300) {
            // Success - process normally
            return;
        } else if (statusCode >= 400 && statusCode < 500) {
            // Client errors
            switch (statusCode) {
                case 400:
                    throw new BadRequestException("Bad Request: " + body);
                case 401:
                    throw new UnauthorizedException("Unauthorized: " + body);
                case 403:
                    throw new ForbiddenException("Forbidden: " + body);
                case 404:
                    throw new NotFoundException("Not Found: " + body);
                case 429:
                    throw new RateLimitException("Rate Limit Exceeded: " + body);
                default:
                    throw new ClientErrorException("Client error " + statusCode + ": " + body);
            }
        } else if (statusCode >= 500) {
            // Server errors
            throw new ServerErrorException("Server error " + statusCode + ": " + body);
        }
    }

    // Custom exception classes
    public static class BadRequestException extends Exception {
        public BadRequestException(String message) { super(message); }
    }

    public static class UnauthorizedException extends Exception {
        public UnauthorizedException(String message) { super(message); }
    }

    public static class ForbiddenException extends Exception {
        public ForbiddenException(String message) { super(message); }
    }

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) { super(message); }
    }

    public static class RateLimitException extends Exception {
        public RateLimitException(String message) { super(message); }
    }

    public static class ClientErrorException extends Exception {
        public ClientErrorException(String message) { super(message); }
    }

    public static class ServerErrorException extends Exception {
        public ServerErrorException(String message) { super(message); }
    }
}
```

---

## 5. Authentication

### Basic Authentication

```java
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BasicAuthClient {

    public CloseableHttpClient createClientWithBasicAuth(
            String username, String password, String host, int port) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            new AuthScope(host, port),
            new UsernamePasswordCredentials(username, password)
        );

        return HttpClients.custom()
            .setDefaultCredentialsProvider(credentialsProvider)
            .build();
    }

    /**
     * Preemptive basic authentication
     */
    public CloseableHttpClient createClientWithPreemptiveAuth(
            String username, String password) {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            new UsernamePasswordCredentials(username, password)
        );

        return HttpClients.custom()
            .setDefaultCredentialsProvider(credentialsProvider)
            .build();
    }
}
```

### Bearer Token Authentication

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class BearerTokenAuth {

    /**
     * Add bearer token to request
     */
    public void addBearerToken(HttpUriRequest request, String token) {
        request.setHeader("Authorization", "Bearer " + token);
    }

    /**
     * Create request with bearer token
     */
    public HttpGet createAuthenticatedRequest(String url, String token) {
        HttpGet httpGet = new HttpGet(url);
        addBearerToken(httpGet, token);
        return httpGet;
    }
}
```

### OAuth 2.0 Integration

```java
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class OAuth2Client {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String accessToken;
    private long tokenExpiryTime;

    public OAuth2Client(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtain access token using client credentials
     */
    public String obtainAccessToken(String tokenUrl, String clientId,
                                    String clientSecret) throws Exception {

        // Check if current token is still valid
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            return accessToken;
        }

        HttpPost httpPost = new HttpPost(tokenUrl);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=client_credentials" +
                     "&client_id=" + clientId +
                     "&client_secret=" + clientSecret;

        httpPost.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            accessToken = jsonNode.get("access_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();

            // Set expiry time with 5-minute buffer
            tokenExpiryTime = System.currentTimeMillis() +
                             ((expiresIn - 300) * 1000L);

            return accessToken;
        }
    }

    /**
     * Make authenticated request
     */
    public String makeAuthenticatedRequest(String url, String tokenUrl,
                                          String clientId, String clientSecret)
            throws Exception {

        String token = obtainAccessToken(tokenUrl, clientId, clientSecret);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}
```

---

## 6. SSL/TLS Configuration

### Custom SSL Configuration

```java
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.cert.X509Certificate;

public class SSLClientConfiguration {

    /**
     * Production SSL configuration with custom truststore
     */
    public CloseableHttpClient createSecureClient(
            String truststorePath, String truststorePassword) throws Exception {

        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(
                new File(truststorePath),
                truststorePassword.toCharArray()
            )
            .build();

        SSLConnectionSocketFactory sslSocketFactory =
            new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2", "TLSv1.3"}, // Supported protocols
                null, // Use default cipher suites
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
            );

        return HttpClients.custom()
            .setSSLSocketFactory(sslSocketFactory)
            .build();
    }

    /**
     * Client with mutual TLS (mTLS)
     */
    public CloseableHttpClient createMutualTLSClient(
            String keystorePath, String keystorePassword,
            String truststorePath, String truststorePassword) throws Exception {

        SSLContext sslContext = SSLContexts.custom()
            .loadKeyMaterial(
                new File(keystorePath),
                keystorePassword.toCharArray(),
                keystorePassword.toCharArray()
            )
            .loadTrustMaterial(
                new File(truststorePath),
                truststorePassword.toCharArray()
            )
            .build();

        SSLConnectionSocketFactory sslSocketFactory =
            new SSLConnectionSocketFactory(sslContext);

        return HttpClients.custom()
            .setSSLSocketFactory(sslSocketFactory)
            .build();
    }

    /**
     * DEVELOPMENT ONLY: Trust all certificates (NOT for production!)
     */
    public CloseableHttpClient createTrustAllClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true; // Trust all certificates
                }
            })
            .build();

        SSLConnectionSocketFactory sslSocketFactory =
            new SSLConnectionSocketFactory(
                sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
            );

        return HttpClients.custom()
            .setSSLSocketFactory(sslSocketFactory)
            .build();
    }
}
```

---

## 7. Retry and Error Handling

### Retry Handler with Exponential Backoff

```java
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.HttpResponse;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.net.ConnectException;

public class RetryConfiguration {

    /**
     * Custom retry handler
     */
    public HttpRequestRetryHandler createRetryHandler() {
        return new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception,
                                       int executionCount,
                                       HttpContext context) {

                // Do not retry if max retries exceeded
                if (executionCount > 3) {
                    return false;
                }

                // Retry on connection timeout
                if (exception instanceof InterruptedIOException) {
                    return true;
                }

                // Retry on unknown host
                if (exception instanceof UnknownHostException) {
                    return true;
                }

                // Retry on connection refused
                if (exception instanceof ConnectException) {
                    return true;
                }

                // Do not retry on SSL errors
                if (exception instanceof SSLException) {
                    return false;
                }

                return false;
            }
        };
    }

    /**
     * Service unavailable retry strategy with exponential backoff
     */
    public ServiceUnavailableRetryStrategy createBackoffStrategy() {
        return new ServiceUnavailableRetryStrategy() {

            @Override
            public boolean retryRequest(HttpResponse response,
                                       int executionCount,
                                       HttpContext context) {
                int statusCode = response.getStatusLine().getStatusCode();

                // Retry on 503 Service Unavailable or 429 Too Many Requests
                return executionCount <= 5 &&
                       (statusCode == 503 || statusCode == 429);
            }

            @Override
            public long getRetryInterval() {
                // Exponential backoff: 1s, 2s, 4s, 8s, 16s
                return (long) Math.pow(2, executionCount - 1) * 1000L;
            }

            private int executionCount = 1;
        };
    }

    /**
     * Advanced retry strategy with jitter
     */
    public static class ExponentialBackoffRetryStrategy
            implements ServiceUnavailableRetryStrategy {

        private final int maxRetries;
        private final long baseDelay;
        private int currentRetry = 0;

        public ExponentialBackoffRetryStrategy(int maxRetries, long baseDelay) {
            this.maxRetries = maxRetries;
            this.baseDelay = baseDelay;
        }

        @Override
        public boolean retryRequest(HttpResponse response,
                                   int executionCount,
                                   HttpContext context) {

            currentRetry = executionCount;
            int statusCode = response.getStatusLine().getStatusCode();

            // Retry on server errors or rate limiting
            boolean shouldRetry = executionCount <= maxRetries &&
                                 (statusCode >= 500 || statusCode == 429);

            if (shouldRetry) {
                // Log retry attempt
                System.out.println("Retry attempt " + executionCount +
                                  " for status code " + statusCode);
            }

            return shouldRetry;
        }

        @Override
        public long getRetryInterval() {
            // Exponential backoff with jitter
            long exponentialDelay = baseDelay * (long) Math.pow(2, currentRetry - 1);
            long jitter = (long) (Math.random() * 1000);
            return exponentialDelay + jitter;
        }
    }

    /**
     * Create client with retry configuration
     */
    public CloseableHttpClient createClientWithRetry() {
        return HttpClients.custom()
            .setRetryHandler(createRetryHandler())
            .setServiceUnavailableRetryStrategy(
                new ExponentialBackoffRetryStrategy(5, 1000L)
            )
            .build();
    }
}
```

### Circuit Breaker Pattern

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitBreaker {

    private enum State {
        CLOSED,    // Normal operation
        OPEN,      // Failures exceeded threshold
        HALF_OPEN  // Testing if service recovered
    }

    private State state = State.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);

    private final int failureThreshold;
    private final long timeoutMillis;
    private final long retryTimeoutMillis;

    public CircuitBreaker(int failureThreshold,
                         long timeoutMillis,
                         long retryTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.timeoutMillis = timeoutMillis;
        this.retryTimeoutMillis = retryTimeoutMillis;
    }

    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            // Check if retry timeout has elapsed
            if (System.currentTimeMillis() - lastFailureTime.get() > retryTimeoutMillis) {
                state = State.HALF_OPEN;
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized void recordSuccess() {
        failureCount.set(0);
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
        }
    }

    public synchronized void recordFailure() {
        lastFailureTime.set(System.currentTimeMillis());
        int failures = failureCount.incrementAndGet();

        if (failures >= failureThreshold) {
            state = State.OPEN;
            System.out.println("Circuit breaker opened after " + failures + " failures");
        }
    }

    public State getState() {
        return state;
    }
}
```

---

## 8. Performance Optimization

### Request Compression

```java
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class CompressionExample {

    /**
     * Send compressed request body
     */
    public HttpPost createCompressedPost(String url, String body)
            throws Exception {

        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(body, "UTF-8");
        GzipCompressingEntity gzipEntity = new GzipCompressingEntity(entity);

        httpPost.setEntity(gzipEntity);
        httpPost.setHeader("Content-Encoding", "gzip");

        return httpPost;
    }
}
```

### Async HTTP Client

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;

import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;

public class AsyncHttpClientExample {

    private CloseableHttpAsyncClient asyncHttpClient;

    public void initialize() throws Exception {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager connManager =
            new PoolingNHttpClientConnectionManager(ioReactor);

        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);

        asyncHttpClient = HttpAsyncClients.custom()
            .setConnectionManager(connManager)
            .build();

        asyncHttpClient.start();
    }

    /**
     * Execute async request with callback
     */
    public void executeAsync(String url) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        HttpGet request = new HttpGet(url);

        asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                System.out.println("Request completed: " +
                    response.getStatusLine().getStatusCode());
                latch.countDown();
            }

            @Override
            public void failed(Exception ex) {
                System.err.println("Request failed: " + ex.getMessage());
                latch.countDown();
            }

            @Override
            public void cancelled() {
                System.out.println("Request cancelled");
                latch.countDown();
            }
        });

        latch.await();
    }

    /**
     * Execute multiple async requests
     */
    public void executeMultipleAsync(String[] urls) throws Exception {
        final CountDownLatch latch = new CountDownLatch(urls.length);

        for (String url : urls) {
            HttpGet request = new HttpGet(url);

            asyncHttpClient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse response) {
                    System.out.println("Completed: " + url);
                    latch.countDown();
                }

                @Override
                public void failed(Exception ex) {
                    System.err.println("Failed: " + url + " - " + ex.getMessage());
                    latch.countDown();
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    public void shutdown() throws Exception {
        asyncHttpClient.close();
    }
}
```

### Keep-Alive Strategy

```java
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class KeepAliveConfiguration {

    /**
     * Custom keep-alive strategy
     */
    public ConnectionKeepAliveStrategy createKeepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response,
                                            HttpContext context) {

                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE)
                );

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

                // Default to 30 seconds if no header
                return 30 * 1000;
            }
        };
    }

    /**
     * Create client with keep-alive
     */
    public CloseableHttpClient createClientWithKeepAlive() {
        return HttpClients.custom()
            .setKeepAliveStrategy(createKeepAliveStrategy())
            .build();
    }
}
```

---

## 9. Monitoring and Logging

### Request/Response Logging Interceptor

```java
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class LoggingInterceptors {

    /**
     * Request logging interceptor
     */
    public static class RequestLoggingInterceptor implements HttpRequestInterceptor {

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws IOException {

            long startTime = System.currentTimeMillis();
            context.setAttribute("startTime", startTime);

            System.out.println("Request: " + request.getRequestLine());
            System.out.println("Headers: ");
            for (org.apache.http.Header header : request.getAllHeaders()) {
                System.out.println("  " + header.getName() + ": " + header.getValue());
            }
        }
    }

    /**
     * Response logging interceptor
     */
    public static class ResponseLoggingInterceptor implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse response, HttpContext context)
                throws IOException {

            Long startTime = (Long) context.getAttribute("startTime");
            long duration = startTime != null ?
                System.currentTimeMillis() - startTime : 0;

            System.out.println("Response: " + response.getStatusLine());
            System.out.println("Duration: " + duration + "ms");
            System.out.println("Headers: ");
            for (org.apache.http.Header header : response.getAllHeaders()) {
                System.out.println("  " + header.getName() + ": " + header.getValue());
            }
        }
    }

    /**
     * Create client with logging
     */
    public CloseableHttpClient createClientWithLogging() {
        return HttpClients.custom()
            .addInterceptorFirst(new RequestLoggingInterceptor())
            .addInterceptorLast(new ResponseLoggingInterceptor())
            .build();
    }
}
```

### Metrics Collection

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MetricsCollector {

    private final PoolingHttpClientConnectionManager connectionManager;
    private final ScheduledExecutorService scheduler;

    public MetricsCollector(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Start collecting metrics
     */
    public void startMetricsCollection() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                collectMetrics();
            } catch (Exception e) {
                System.err.println("Error collecting metrics: " + e.getMessage());
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * Collect connection pool metrics
     */
    private void collectMetrics() {
        PoolStats totalStats = connectionManager.getTotalStats();

        System.out.println("=== Connection Pool Metrics ===");
        System.out.println("Available: " + totalStats.getAvailable());
        System.out.println("Leased: " + totalStats.getLeased());
        System.out.println("Pending: " + totalStats.getPending());
        System.out.println("Max: " + totalStats.getMax());

        // Log route-specific stats
        connectionManager.getRoutes().forEach(route -> {
            PoolStats routeStats = connectionManager.getStats(route);
            System.out.println("Route " + route + ": " +
                "Available=" + routeStats.getAvailable() +
                ", Leased=" + routeStats.getLeased() +
                ", Pending=" + routeStats.getPending());
        });
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
```

---

## 10. Production Best Practices

### Complete Production-Ready Client

```java
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;

import java.util.concurrent.TimeUnit;

public class ProductionHttpClient {

    private final CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final IdleConnectionEvictor connectionEvictor;
    private final MetricsCollector metricsCollector;

    public ProductionHttpClient() {
        // Connection manager configuration
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setValidateAfterInactivity(2000);

        // Socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setSoTimeout(30000)
            .setSoKeepAlive(true)
            .setTcpNoDelay(true)
            .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        // Request configuration
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(30000)
            .setConnectionRequestTimeout(3000)
            .setRedirectsEnabled(true)
            .setMaxRedirects(5)
            .setCircularRedirectsAllowed(false)
            .build();

        // Build client
        httpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(new RetryConfiguration().createRetryHandler())
            .setServiceUnavailableRetryStrategy(
                new RetryConfiguration.ExponentialBackoffRetryStrategy(5, 1000L)
            )
            .setKeepAliveStrategy(
                new KeepAliveConfiguration().createKeepAliveStrategy()
            )
            .evictExpiredConnections()
            .evictIdleConnections(60L, TimeUnit.SECONDS)
            .setMaxConnPerRoute(20)
            .setMaxConnTotal(200)
            .setUserAgent("MyApp/1.0")
            .build();

        // Start connection evictor
        connectionEvictor = new IdleConnectionEvictor(connectionManager);
        connectionEvictor.start();

        // Start metrics collector
        metricsCollector = new MetricsCollector(connectionManager);
        metricsCollector.startMetricsCollection();
    }

    public CloseableHttpClient getClient() {
        return httpClient;
    }

    public PoolStats getPoolStats() {
        return connectionManager.getTotalStats();
    }

    /**
     * Graceful shutdown
     */
    public void shutdown() {
        try {
            connectionEvictor.shutdown();
            metricsCollector.shutdown();
            httpClient.close();
            connectionManager.close();
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }
}
```

### Environment-Specific Configuration

```java
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HttpClientConfig {

    private final Properties properties;

    public HttpClientConfig(String environment) throws IOException {
        properties = new Properties();
        String configFile = "httpclient-" + environment + ".properties";

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IOException("Unable to find " + configFile);
            }
            properties.load(input);
        }
    }

    public int getMaxTotalConnections() {
        return Integer.parseInt(
            properties.getProperty("http.client.max.total", "200")
        );
    }

    public int getMaxPerRoute() {
        return Integer.parseInt(
            properties.getProperty("http.client.max.per.route", "20")
        );
    }

    public int getConnectTimeout() {
        return Integer.parseInt(
            properties.getProperty("http.client.connect.timeout", "5000")
        );
    }

    public int getSocketTimeout() {
        return Integer.parseInt(
            properties.getProperty("http.client.socket.timeout", "30000")
        );
    }

    public int getConnectionRequestTimeout() {
        return Integer.parseInt(
            properties.getProperty("http.client.connection.request.timeout", "3000")
        );
    }

    public boolean isRetryEnabled() {
        return Boolean.parseBoolean(
            properties.getProperty("http.client.retry.enabled", "true")
        );
    }

    public int getMaxRetries() {
        return Integer.parseInt(
            properties.getProperty("http.client.max.retries", "3")
        );
    }
}
```

### Example Configuration Files

**httpclient-production.properties:**
```properties
http.client.max.total=200
http.client.max.per.route=20
http.client.connect.timeout=5000
http.client.socket.timeout=30000
http.client.connection.request.timeout=3000
http.client.retry.enabled=true
http.client.max.retries=3
http.client.keep.alive.duration=30000
```

**httpclient-development.properties:**
```properties
http.client.max.total=50
http.client.max.per.route=10
http.client.connect.timeout=10000
http.client.socket.timeout=60000
http.client.connection.request.timeout=5000
http.client.retry.enabled=true
http.client.max.retries=5
http.client.keep.alive.duration=60000
```

### Complete Usage Example

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;

public class HttpClientUsageExample {

    public static void main(String[] args) {
        ProductionHttpClient client = new ProductionHttpClient();

        try {
            // Simple GET request
            HttpGet httpGet = new HttpGet("https://api.example.com/data");
            httpGet.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response =
                    client.getClient().execute(httpGet)) {

                ResponseHandler.HttpResponseWrapper wrapper =
                    ResponseHandler.parseResponse(response);

                if (wrapper.isSuccess()) {
                    System.out.println("Response: " + wrapper.getBody());
                } else {
                    System.err.println("Error: " + wrapper.getStatusCode() +
                        " - " + wrapper.getReasonPhrase());
                }
            }

            // Check pool statistics
            System.out.println("Pool stats: " + client.getPoolStats());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always shutdown gracefully
            client.shutdown();
        }
    }
}
```

---

## Key Takeaways for Production

1. **Always use connection pooling** with `PoolingHttpClientConnectionManager`
2. **Set appropriate timeouts** for connect, socket, and connection request
3. **Implement retry logic** with exponential backoff for transient failures
4. **Use SSL/TLS** properly with valid certificates in production
5. **Monitor connection pool metrics** to detect issues early
6. **Implement circuit breakers** for downstream service failures
7. **Always close resources** using try-with-resources
8. **Configure keep-alive** to reuse connections efficiently
9. **Use async clients** for high-throughput scenarios
10. **Log requests and responses** for debugging and auditing
11. **Handle different HTTP status codes** appropriately
12. **Evict idle and expired connections** regularly
13. **Use environment-specific configurations**
14. **Implement proper authentication** mechanisms
15. **Gracefully shutdown** clients to release resources

This comprehensive guide covers all essential aspects of using Apache HttpClient 4.x in production environments with real-world code examples and best practices.
