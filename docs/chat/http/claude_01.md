# Comprehensive Guide to Apache HttpClient 4.x for Production Environments

## Table of Contents
1. Introduction and Setup
2. Basic Configuration
3. Connection Management
4. Request Execution Patterns
5. Error Handling and Retry Logic
6. Security and SSL/TLS
7. Performance Optimization
8. Monitoring and Logging
9. Production-Ready Examples

## 1. Introduction and Setup

Apache HttpClient 4.x is a robust, feature-rich HTTP client library for Java applications. For production use, proper configuration is critical for performance, reliability, and resource management.

### Maven Dependencies

```xml
<dependencies>
    <!-- Core HttpClient -->
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

    <!-- For JSON handling (optional) -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>
```

## 2. Production-Ready HttpClient Configuration

### Complete Configuration Class

```java
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

public class HttpClientFactory {

    private static final int CONNECT_TIMEOUT = 5000; // 5 seconds
    private static final int SOCKET_TIMEOUT = 30000; // 30 seconds
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;
    private static final int MAX_TOTAL_CONNECTIONS = 200;
    private static final int MAX_PER_ROUTE = 20;
    private static final int EVICTION_IDLE_TIME = 30; // seconds

    public static CloseableHttpClient createProductionClient() {
        // Connection pool manager
        PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        // Socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setSoTimeout(SOCKET_TIMEOUT)
            .setTcpNoDelay(true)
            .setSoKeepAlive(true)
            .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        // Request configuration
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .setExpectContinueEnabled(true)
            .build();

        // Build client
        CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .evictExpiredConnections()
            .evictIdleConnections(EVICTION_IDLE_TIME, TimeUnit.SECONDS)
            .setRetryHandler(new CustomRetryHandler())
            .setServiceUnavailableRetryStrategy(new CustomServiceUnavailableRetryStrategy())
            .build();

        // Start idle connection monitor thread
        startIdleConnectionMonitor(connectionManager);

        return httpClient;
    }

    private static void startIdleConnectionMonitor(
            PoolingHttpClientConnectionManager connectionManager) {
        Thread monitorThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(30000);
                    connectionManager.closeExpiredConnections();
                    connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("HttpClient-IdleConnectionMonitor");
        monitorThread.start();
    }
}
```

## 3. Connection Management Best Practices

### Connection Pool Manager with JMX Monitoring

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

public class ConnectionPoolMonitor {
    private final PoolingHttpClientConnectionManager connectionManager;

    public ConnectionPoolMonitor(PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public PoolStats getTotalStats() {
        return connectionManager.getTotalStats();
    }

    public void logPoolStats() {
        PoolStats stats = getTotalStats();
        System.out.println("Connection Pool Stats:");
        System.out.println("  Available: " + stats.getAvailable());
        System.out.println("  Leased: " + stats.getLeased());
        System.out.println("  Pending: " + stats.getPending());
        System.out.println("  Max: " + stats.getMax());
    }

    public void adjustPoolSize(int newMaxTotal, int newMaxPerRoute) {
        connectionManager.setMaxTotal(newMaxTotal);
        connectionManager.setDefaultMaxPerRoute(newMaxPerRoute);
    }
}
```

## 4. Request Execution Patterns

### Generic HTTP Request Executor

```java
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpRequestExecutor {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestExecutor.class);
    private final CloseableHttpClient httpClient;

    public HttpRequestExecutor(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpResult executeGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return executeRequest(request);
    }

    public HttpResult executePost(String url, String jsonBody) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
        return executeRequest(request);
    }

    public HttpResult executePut(String url, String jsonBody) throws IOException {
        HttpPut request = new HttpPut(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
        return executeRequest(request);
    }

    public HttpResult executeDelete(String url) throws IOException {
        HttpDelete request = new HttpDelete(url);
        return executeRequest(request);
    }

    private HttpResult executeRequest(HttpRequestBase request) throws IOException {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;

        try {
            // Add common headers
            request.setHeader("User-Agent", "MyApp/1.0");
            request.setHeader("Accept", "application/json");

            logger.info("Executing {} request to {}", request.getMethod(), request.getURI());

            response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = extractResponseBody(response);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request completed in {}ms with status {}", duration, statusCode);

            return new HttpResult(statusCode, responseBody, duration);

        } catch (IOException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Request failed after {}ms: {}", duration, e.getMessage());
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.warn("Error closing response", e);
                }
            }
        }
    }

    private String extractResponseBody(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return "";
        }

        try {
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } finally {
            EntityUtils.consume(entity); // Ensures the connection is released
        }
    }
}

// Result class
class HttpResult {
    private final int statusCode;
    private final String body;
    private final long durationMs;

    public HttpResult(int statusCode, String body, long durationMs) {
        this.statusCode = statusCode;
        this.body = body;
        this.durationMs = durationMs;
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public long getDurationMs() { return durationMs; }
    public boolean isSuccessful() { return statusCode >= 200 && statusCode < 300; }
}
```

## 5. Error Handling and Retry Logic

### Custom Retry Handler

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        logger.warn("Request failed (attempt {}): {}", executionCount, exception.getMessage());

        if (executionCount > MAX_RETRIES) {
            logger.error("Max retry attempts ({}) exceeded", MAX_RETRIES);
            return false;
        }

        // Don't retry on certain exception types
        for (Class<? extends IOException> exceptionClass : NON_RETRIABLE_EXCEPTIONS) {
            if (exceptionClass.isInstance(exception)) {
                logger.warn("Non-retriable exception: {}", exceptionClass.getSimpleName());
                return false;
            }
        }

        // Don't retry if request has been sent (for idempotency)
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();

        // Retry only idempotent requests
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);

        if (idempotent) {
            logger.info("Retrying idempotent request (attempt {})", executionCount + 1);

            // Exponential backoff
            try {
                Thread.sleep(1000L * executionCount);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            return true;
        }

        return false;
    }
}
```

### Service Unavailable Retry Strategy

```java
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
    private static final Logger logger = LoggerFactory.getLogger(CustomServiceUnavailableRetryStrategy.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL = 2000; // 2 seconds

    @Override
    public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
        int statusCode = response.getStatusLine().getStatusCode();

        // Retry on 503 Service Unavailable or 429 Too Many Requests
        boolean shouldRetry = (statusCode == 503 || statusCode == 429)
                              && executionCount <= MAX_RETRIES;

        if (shouldRetry) {
            logger.warn("Received {} status, will retry (attempt {})",
                       statusCode, executionCount);
        }

        return shouldRetry;
    }

    @Override
    public long getRetryInterval() {
        return RETRY_INTERVAL;
    }
}
```

## 6. Security and SSL/TLS Configuration

### Custom SSL Configuration

```java
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SSLConfigurationFactory {

    // Production SSL configuration with certificate validation
    public static SSLConnectionSocketFactory createProductionSSL()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(null, (chain, authType) -> {
                // Implement custom trust validation if needed
                // For production, validate against trusted CAs
                return false; // Use default trust store
            })
            .build();

        return new SSLConnectionSocketFactory(
            sslContext,
            new String[]{"TLSv1.2", "TLSv1.3"}, // Supported protocols
            null, // Use default cipher suites
            SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );
    }

    // For development only - accepts self-signed certificates
    public static SSLConnectionSocketFactory createTrustAllSSL()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build();

        return new SSLConnectionSocketFactory(
            sslContext,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        );
    }
}
```

## 7. Performance Optimization

### Request Compression

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CompressionExample {

    public static CloseableHttpClient createClientWithCompression() {
        return HttpClients.custom()
            .addInterceptorFirst((org.apache.http.HttpRequestInterceptor) (request, context) -> {
                // Request compression
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip, deflate");
                }
            })
            .build();
    }
}
```

### Response Streaming for Large Payloads

```java
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StreamingResponseHandler {

    public void processLargeResponse(CloseableHttpClient httpClient, String url)
            throws IOException {

        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream inputStream = entity.getContent();
                     BufferedReader reader = new BufferedReader(
                         new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Process line by line to avoid loading entire response into memory
                        processLine(line);
                    }
                }
            }
        }
    }

    private void processLine(String line) {
        // Process each line of the response
    }
}
```

## 8. Monitoring and Metrics

### Request Metrics Collector

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HttpClientMetrics {
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successfulRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private final ConcurrentHashMap<Integer, AtomicInteger> statusCodeCounts =
        new ConcurrentHashMap<>();

    public void recordRequest(int statusCode, long responseTimeMs, boolean success) {
        totalRequests.incrementAndGet();
        totalResponseTime.addAndGet(responseTimeMs);

        if (success) {
            successfulRequests.incrementAndGet();
        } else {
            failedRequests.incrementAndGet();
        }

        statusCodeCounts
            .computeIfAbsent(statusCode, k -> new AtomicInteger(0))
            .incrementAndGet();
    }

    public MetricsSnapshot getSnapshot() {
        int total = totalRequests.get();
        int successful = successfulRequests.get();
        int failed = failedRequests.get();
        long avgResponseTime = total > 0 ? totalResponseTime.get() / total : 0;

        return new MetricsSnapshot(total, successful, failed, avgResponseTime,
                                  new ConcurrentHashMap<>(statusCodeCounts));
    }

    public void reset() {
        totalRequests.set(0);
        successfulRequests.set(0);
        failedRequests.set(0);
        totalResponseTime.set(0);
        statusCodeCounts.clear();
    }
}

class MetricsSnapshot {
    private final int totalRequests;
    private final int successfulRequests;
    private final int failedRequests;
    private final long averageResponseTimeMs;
    private final ConcurrentHashMap<Integer, AtomicInteger> statusCodeCounts;

    public MetricsSnapshot(int totalRequests, int successfulRequests, int failedRequests,
                          long averageResponseTimeMs,
                          ConcurrentHashMap<Integer, AtomicInteger> statusCodeCounts) {
        this.totalRequests = totalRequests;
        this.successfulRequests = successfulRequests;
        this.failedRequests = failedRequests;
        this.averageResponseTimeMs = averageResponseTimeMs;
        this.statusCodeCounts = statusCodeCounts;
    }

    @Override
    public String toString() {
        return String.format(
            "Total: %d, Successful: %d, Failed: %d, Avg Response Time: %dms",
            totalRequests, successfulRequests, failedRequests, averageResponseTimeMs
        );
    }

    // Getters omitted for brevity
}
```

## 9. Complete Production Example

### Spring-Based HttpClient Service

```java
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Service
public class HttpClientService {
    private final CloseableHttpClient httpClient;
    private final HttpRequestExecutor requestExecutor;
    private final HttpClientMetrics metrics;

    public HttpClientService() {
        this.httpClient = HttpClientFactory.createProductionClient();
        this.requestExecutor = new HttpRequestExecutor(httpClient);
        this.metrics = new HttpClientMetrics();
    }

    public HttpResult get(String url) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            HttpResult result = requestExecutor.executeGet(url);
            recordMetrics(result, startTime);
            return result;
        } catch (IOException e) {
            recordFailure(startTime);
            throw e;
        }
    }

    public HttpResult post(String url, String jsonBody) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            HttpResult result = requestExecutor.executePost(url, jsonBody);
            recordMetrics(result, startTime);
            return result;
        } catch (IOException e) {
            recordFailure(startTime);
            throw e;
        }
    }

    private void recordMetrics(HttpResult result, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        metrics.recordRequest(result.getStatusCode(), duration, result.isSuccessful());
    }

    private void recordFailure(long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        metrics.recordRequest(0, duration, false);
    }

    public MetricsSnapshot getMetrics() {
        return metrics.getSnapshot();
    }

    @PreDestroy
    public void cleanup() {
        try {
            httpClient.close();
        } catch (IOException e) {
            // Log error
        }
    }
}
```

### Usage Example with Error Handling

```java
public class HttpClientUsageExample {

    public static void main(String[] args) {
        HttpClientService service = new HttpClientService();

        try {
            // GET request
            HttpResult getResult = service.get("https://api.example.com/users/123");

            if (getResult.isSuccessful()) {
                System.out.println("Response: " + getResult.getBody());
            } else {
                System.err.println("Request failed with status: " + getResult.getStatusCode());
            }

            // POST request
            String jsonPayload = "{\"name\":\"John\",\"email\":\"john@example.com\"}";
            HttpResult postResult = service.post("https://api.example.com/users", jsonPayload);

            System.out.println("POST completed in " + postResult.getDurationMs() + "ms");

            // Check metrics
            MetricsSnapshot metrics = service.getMetrics();
            System.out.println("Metrics: " + metrics);

        } catch (IOException e) {
            System.err.println("HTTP request failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            service.cleanup();
        }
    }
}
```

## Production Checklist

When deploying HttpClient in production, ensure you:

1. **Connection Pooling**: Always use PoolingHttpClientConnectionManager
2. **Timeouts**: Set appropriate connect, socket, and connection request timeouts
3. **Resource Cleanup**: Always close responses and the client properly
4. **Retry Logic**: Implement smart retry strategies for transient failures
5. **Monitoring**: Track request metrics, pool statistics, and error rates
6. **SSL/TLS**: Use strong protocols (TLSv1.2+) and proper certificate validation
7. **Idle Connection Eviction**: Regularly clean up stale connections
8. **Thread Safety**: HttpClient instances are thread-safe and should be reused
9. **Logging**: Implement comprehensive logging for debugging
10. **Circuit Breakers**: Consider adding circuit breaker patterns for failing services

This guide provides production-ready patterns for Apache HttpClient 4.x that prioritize reliability, performance, and maintainability.
