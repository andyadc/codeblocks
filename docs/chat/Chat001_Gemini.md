Of course. Here is a comprehensive guide to using Apache HttpClient 4.x in production environments, complete with code
examples, best practices, and configuration templates.

## Basic Configuration Examples

Properly configuring your `HttpClient` is crucial for performance and stability. A shared, centrally
configured `HttpClient` instance is the cornerstone of a robust application.

### Connection Pooling & Timeouts

Connection pooling is essential for high-throughput applications as it reuses existing connections, avoiding the
overhead of establishing new TCP connections and performing SSL/TLS handshakes for every request.

```java
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientFactory {

    public static CloseableHttpClient createHttpClient() {
        // Create a connection manager with optimal settings
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // Set the total number of connections
        connectionManager.setMaxTotal(200);
        // Set the maximum number of connections per route (host)
        connectionManager.setDefaultMaxPerRoute(20);

        // Configure default request settings
        RequestConfig requestConfig = RequestConfig.custom()
                // Timeout to establish a connection with the host
                .setConnectTimeout(5000) // 5 seconds
                // Timeout for waiting for data from the host
                .setSocketTimeout(5000) // 5 seconds
                // Timeout to get a connection from the connection pool
                .setConnectionRequestTimeout(1000) // 1 second
                .build();

        // Build the HttpClient
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
```

-----

### Proxy Settings

If your application is behind a corporate firewall, you'll need to configure a proxy.

```java
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ProxyConfiguration {

    public static CloseableHttpClient createHttpClientWithProxy() {
        HttpHost proxy = new HttpHost("proxy.example.com", 8080, "http");

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();
    }
}
```

-----

### SSL/TLS Configuration with Custom Trust Store

To connect to services using self-signed certificates or a custom Certificate Authority (CA), you need to configure a
custom SSL context.

```java
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SslConfiguration {

    public static CloseableHttpClient createHttpsClientWithCustomTrustStore() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        // Load the custom trust store
        try (FileInputStream instream = new FileInputStream(new File("path/to/your/truststore.jks"))) {
            trustStore.load(instream, "your_password".toCharArray());
        }

        // Create an SSL context with the custom trust store
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()) // Allows self-signed certs
                .build();

        // Create a socket factory with the custom SSL context
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2", "TLSv1.3"}, // Enforce modern TLS versions
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
    }
}
```

-----

### Custom Retry Mechanisms

HttpClient can automatically retry failed requests. It's often best to use a custom retry handler to control *what*
and *when* to retry.

```java
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class RetryConfiguration {

    public static CloseableHttpClient createHttpClientWithRetries() {
        HttpRequestRetryHandler retryHandler = (exception, executionCount, context) -> {
            // Do not retry more than 3 times
            if (executionCount > 3) {
                return false;
            }
            // Add logic for specific exceptions to retry on (e.g., network issues)
            if (exception instanceof java.net.ConnectException || exception instanceof java.net.SocketTimeoutException) {
                System.out.println("Retrying request, attempt " + executionCount);
                return true;
            }
            return false;
        };

        return HttpClients.custom()
                .setRetryHandler(retryHandler)
                .build();
    }
}
```

-----

### Connection Keep-Alive Settings

The `Keep-Alive` strategy determines how long a persistent connection should remain open.

```java
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

public class KeepAliveConfiguration {

    public static CloseableHttpClient createHttpClientWithKeepAlive() {
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
            // Honor 'Keep-Alive' header from server
            // If not present, keep alive for 30 seconds
            return 30 * 1000;
        };

        return HttpClients.custom()
                .setKeepAliveStrategy(keepAliveStrategy)
                .build();
    }
}
```

-----

## Authentication Examples

HttpClient provides built-in support for standard authentication schemes and allows for custom implementations.

### Basic Authentication

```java
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BasicAuthExample {

    public static CloseableHttpClient createClientWithBasicAuth() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("api.example.com", 443),
                new UsernamePasswordCredentials("username", "password"));

        return HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }
}
```

### Bearer Token Authentication

Bearer tokens are typically sent in the `Authorization` header. This is often handled manually or with an interceptor.

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import java.util.ArrayList;
import java.util.List;

public class BearerTokenExample {

    public HttpGet createGetRequestWithBearerToken(String url, String token) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + token);
        return request;
    }
}
```

### Cookie Handling

HttpClient can manage cookies automatically using a `CookieStore`.

```java
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CookieHandlingExample {

    public static CloseableHttpClient createClientWithCookieStore() {
        // Create a cookie store
        CookieStore cookieStore = new BasicCookieStore();

        // Build a client with the cookie store
        return HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
    }
}
```

-----

## Request/Response Handling

This section covers common operations for sending and receiving data.

### Standard Methods & JSON Handling

Here's an example of a POST request with a JSON payload using the Jackson library.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class JsonHandling {

    public static class User {
        public String name;
        public String email;
    }

    public void postJson(CloseableHttpClient client) throws Exception {
        HttpPost post = new HttpPost("https://api.example.com/users");

        User user = new User();
        user.name = "John Doe";
        user.email = "john.doe@example.com";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        StringEntity entity = new StringEntity(json);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        try (CloseableHttpResponse response = client.execute(post)) {
            System.out.println(response.getStatusLine());
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
        }
    }
}
```

### Multipart File Uploads

For uploading files, use `MultipartEntityBuilder`.

```java
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.File;

public class FileUploadExample {

    public void uploadFile(CloseableHttpClient client) throws Exception {
        HttpPost uploadFile = new HttpPost("https://api.example.com/upload");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("field1", "value1", ContentType.TEXT_PLAIN);

        // Attach a file
        File file = new File("path/to/your/file.txt");
        builder.addBinaryBody(
            "file",
            file,
            ContentType.APPLICATION_OCTET_STREAM,
            file.getName()
        );

        uploadFile.setEntity(builder.build());

        try (CloseableHttpResponse response = client.execute(uploadFile)) {
            System.out.println(response.getStatusLine());
            // Handle response
        }
    }
}
```

### Streaming Large Files

To handle large responses without loading them entirely into memory, process the entity's input stream directly.

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StreamingExample {
    public void downloadLargeFile(CloseableHttpClient client) throws Exception {
        HttpGet request = new HttpGet("https://example.com/largefile.zip");
        try (CloseableHttpResponse response = client.execute(request)) {
            InputStream source = response.getEntity().getContent();
            try (FileOutputStream out = new FileOutputStream(new File("downloaded.zip"))) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = source.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
```

-----

## Advanced Features

Leverage interceptors and custom handlers for cross-cutting concerns.

### Request Interceptors

Interceptors can modify requests before they're sent, perfect for adding common headers like an `Authorization`
or `User-Agent`.

```java
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import java.io.IOException;

public class InterceptorExample {

    public static CloseableHttpClient createClientWithInterceptor() {
        HttpRequestInterceptor authInterceptor = (request, context) -> {
            // Add a static bearer token to every request
            request.addHeader("Authorization", "Bearer your-static-token");
            request.addHeader("User-Agent", "MyAwesomeApp/1.0");
        };

        return HttpClients.custom()
                .addInterceptorLast(authInterceptor)
                .build();
    }
}
```

### Response Handlers

A `ResponseHandler` simplifies response processing and ensures resources are closed correctly. It's the recommended way
to execute requests.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class ResponseHandlerExample {

    public static class User {
        public String name;
        public String email;
    }

    public void getUser(CloseableHttpClient client) throws Exception {
        HttpGet get = new HttpGet("https://api.example.com/users/1");

        // Create a custom response handler
        ResponseHandler<User> handler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return null;
                }
                String json = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, User.class);
            } else {
                throw new IOException("Unexpected response status: " + status);
            }
        };

        User user = client.execute(get, handler);
        System.out.println("User name: " + user.name);
    }
}
```

-----

## Error Handling

Robust error handling is non-negotiable in production.

- **`java.net.ConnectException`**: Network error while trying to establish a connection. Often retryable.
- **`java.net.SocketTimeoutException`**: Timeout while waiting for data from the server after a connection has been
  established.
- **`org.apache.http.conn.ConnectTimeoutException`**: Timeout while waiting for a connection from the connection
  manager.
- **`javax.net.ssl.SSLHandshakeException`**: SSL/TLS handshake failure. Usually due to trust issues (e.g., untrusted
  certificate) or protocol mismatches.

<!-- end list -->

```java
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ErrorHandlingExample {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingExample.class);

    public void executeRequestWithHandling(CloseableHttpClient client) {
        HttpGet get = new HttpGet("http://example.com");
        try (CloseableHttpResponse response = client.execute(get)) {
            // Process successful response
        } catch (IOException e) {
            // Catches ConnectException, SocketTimeoutException, etc.
            logger.error("HTTP request failed", e);
            // Implement fallback logic or re-throw as a custom application exception
        } finally {
            // With try-with-resources on the response, the connection is always released.
            // When not using try-with-resources, you MUST manually close the response entity.
        }
    }
}
```

-----

## Monitoring and Metrics

Monitoring the `HttpClient`'s connection pool is vital for diagnosing performance bottlenecks.

```java
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

public class MonitoringExample {

    public void logPoolStats(PoolingHttpClientConnectionManager connectionManager) {
        PoolStats stats = connectionManager.getTotalStats();
        System.out.println("Max connections: " + stats.getMax());
        System.out.println("Available connections: " + stats.getAvailable());
        System.out.println("Leased connections: " + stats.getLeased());
        System.out.println("Pending connections: " + stats.getPending());
    }
}
```

For comprehensive monitoring, integrate with a metrics library like **Micrometer** or **Dropwizard Metrics**. These
libraries can automatically instrument `HttpClient` to provide detailed metrics on request latency, success/failure
rates, and more.

-----

## Best Practices & Common Pitfalls

### ✅ Best Practices

1. **Singleton `HttpClient`**: Create a single `CloseableHttpClient` instance and share it across your application for
   its entire lifecycle. It's thread-safe.
2. **Use Connection Pooling**: Always use a `PoolingHttpClientConnectionManager`. Tune `setMaxTotal`
   and `setDefaultMaxPerRoute` based on your application's needs.
3. **Use `ResponseHandler`**: This pattern ensures that the underlying connection is always released back to the pool,
   preventing connection leaks.
4. **Close Resources**: If not using `ResponseHandler`, always close the `CloseableHttpResponse` and the entity's
   content stream in a `finally` block or use try-with-resources.
5. **Set Timeouts**: Always configure connection, socket, and connection request timeouts to prevent threads from
   blocking indefinitely.
6. **Use Interceptors**: Handle cross-cutting concerns like logging, authentication, and custom headers with
   interceptors.
7. **Enable Gzip**: HttpClient handles gzip compression automatically if the server sends the `Content-Encoding: gzip`
   header.

### 🛑 Common Pitfalls

1. **Creating a new `HttpClient` for each request**: This is extremely inefficient. It bypasses connection pooling and
   adds significant overhead.
2. **Not Releasing Connections**: Failing to close the response or its entity stream will cause connections to be "
   leased" forever, eventually exhausting the connection pool.
3. **Ignoring Timeouts**: Not setting timeouts can cause your application to become unresponsive under network or server
   load.
4. **Blocking in `ResponseHandler`**: Avoid long-running or blocking operations inside a `ResponseHandler`, as it holds
   onto a connection from the pool.
5. **Mutable `RequestConfig`**: Be careful when modifying `RequestConfig` on a per-request basis in a multi-threaded
   environment. Create a new builder from the default config (`RequestConfig.copy(defaultConfig).build()`).
