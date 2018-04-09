package com.andyadc.scaffold.showcase.common.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;

import java.util.concurrent.TimeUnit;

/**
 * HttpClient 4.5.3
 * zhangkaitao
 *
 * @author andaicheng
 * @version 2017/4/12
 */
public class HttpClientUtil {

    static PoolingHttpClientConnectionManager manager = null;
    static CloseableHttpClient httpClient = null;

    public static synchronized CloseableHttpClient getHttpClient() {
        if (httpClient == null) {

            //注册访问协议相关的Socket工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                    .build();

            //HttpConnection 工厂; 配置写请求/解析响应处理器
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(
                    DefaultHttpRequestWriterFactory.INSTANCE,
                    DefaultHttpResponseParserFactory.INSTANCE
            );

            //DNS 解析器
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

            //创建池化连接管理器
            manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);

            //默认为Socket配置
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            manager.setDefaultSocketConfig(socketConfig);

            manager.setMaxTotal(300); // 设置整个连接池的最大连接数
            //每个路由的默认最大连接, 每个路由实际最大连接数默认为
            //DefaultMaxPerRoute控制, 而MaxTotal是控制整个池子最大数
            //设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)
            //路由是对MaxTotal的细分
            manager.setDefaultMaxPerRoute(200);// 每个路由最大连接数
            // 从连接池获取连接时, 链接不活跃多长时间需要进行一次验证, 默认2s
            manager.setValidateAfterInactivity(5 * 1000);

            // 默认请求配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(2 * 1000) // 设置连接超时时间, 2s
                    .setSocketTimeout(5 * 1000) // 设置等待数据超时时间, 5s
                    .setConnectionRequestTimeout(2000)  // 设置从连接池获取连接的等待超时时间
                    .build();

            // 创建HttpClient
            httpClient = HttpClients.custom()
                    .setConnectionManager(manager)
                    .setConnectionManagerShared(false)  // 连接池不是共享模式
                    .evictIdleConnections(60, TimeUnit.SECONDS) // 定期回收空闲连接
                    .evictExpiredConnections()  // 定期回收过期连接
                    .setConnectionTimeToLive(60, TimeUnit.SECONDS)  //连接存活时间, 如果不设置, 则根据长连接信息决定
                    .setDefaultRequestConfig(requestConfig) //设置默认请求配置
                    .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)    //连接重用策略
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)  // 长连接策略
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))  //设置重试次数, 默认3次; 当前是禁用掉(
                    .build();

            // JVM 停止或者重启时, 关闭连接池释放资源
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        httpClient.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        return httpClient;
    }
}
