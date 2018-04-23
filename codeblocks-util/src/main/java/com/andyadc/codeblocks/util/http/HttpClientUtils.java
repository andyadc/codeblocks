package com.andyadc.codeblocks.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HttpClientUtils {

    private static final CloseableHttpClient httpClient;
    private static final String CHARSET_UTF8 = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    private HttpClientUtils() {
    }

    public static String doGet(String url) {
        return doGet(url, null, null, CHARSET_UTF8);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null, CHARSET_UTF8);
    }

    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers) {
        return doGet(url, params, headers, CHARSET_UTF8);
    }

    public static String doPost(String url, Map<String, Object> params) {
        return doPost(url, params, null, CHARSET_UTF8);
    }

    public static String doPost(String url, Map<String, Object> params, Map<String, String> headers) {
        return doPost(url, params, headers, CHARSET_UTF8);
    }

    public static String doPost(String url, String sendData) {
        return doPost(url, sendData, null, CHARSET_UTF8);
    }

    public static String doPost(String url, String sendData, Map<String, String> headers) {
        return doPost(url, sendData, headers, CHARSET_UTF8);
    }

    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return response
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), Objects.toString(value)));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }

            HttpGet httpGet = new HttpGet(url);

            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = i.next();
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpGet.abort();
                throw new RuntimeException("HttpClient, error status code: " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET_UTF8);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET error: " + url, e);
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return response
     */
    public static String doPost(String url, Map<String, Object> params, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String value = Objects.toString(entry.getValue());
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = Objects.toString(i.next());
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            if (pairs != null && !pairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET_UTF8));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient, error status code: " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET_UTF8);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("HTTP POST error: " + url, e);
        }
    }

    public static String doPost(String url, String sendData, Map<String, String> headers, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
                Set<String> keys = headers.keySet();
                for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                    String key = Objects.toString(i.next());
                    httpPost.addHeader(key, headers.get(key));
                }
            }

            StringEntity reqEntity = new StringEntity(sendData, CHARSET_UTF8);
            reqEntity.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(reqEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code: " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET_UTF8);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("HTTP POST error: " + url, e);
        }
    }

    public static CloseableHttpClient getHttpClientInstance() {
        return httpClient;
    }

}
