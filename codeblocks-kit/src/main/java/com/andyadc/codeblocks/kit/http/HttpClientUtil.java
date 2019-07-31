package com.andyadc.codeblocks.kit.http;

import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final CloseableHttpClient httpClient;
	private static final String CHARSET_UTF8 = "UTF-8";

	static {
		httpClient = HttpClientBuilder.buildHttpClient();
	}

	private HttpClientUtil() {
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

	/**
	 * @param url      request url
	 * @param sendData json data
	 * @return response
	 */
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
	public static String doGet(String url,
							   Map<String, Object> params,
							   Map<String, String> headers,
							   String charset) {

		if (StringUtil.isBlank(url)) {
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
			addHeaders(httpGet, headers);

			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				httpGet.abort();
				logger.error("Error status code: " + statusCode);
				return "";
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET_UTF8);
			}
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			logger.error("HTTP GET error: " + url, e);
		}
		return "";
	}

	/**
	 * HTTP Post 获取内容
	 *
	 * @param url     请求的url地址
	 * @param params  请求的参数
	 * @param charset 编码格式
	 * @return response
	 */
	public static String doPost(String url,
								Map<String, Object> params,
								Map<String, String> headers,
								String charset) {

		if (StringUtil.isBlank(url)) {
			return null;
		}

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

		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost, headers);

		try {
			if (pairs != null && !pairs.isEmpty()) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
			}

			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				httpPost.abort();
				logger.error("Error status code: " + statusCode);
				return "";
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET_UTF8);
			}
			// Ensure that the entity content has been fully consumed and the underlying stream has been closed.
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			logger.error("HTTP POST error: " + url, e);
		}
		return "";
	}

	public static String doPost(String url, String sendData, Map<String, String> headers, String charset) {
		if (StringUtil.isBlank(url)) {
			return null;
		}

		HttpPost httpPost = new HttpPost(url);
		addHeaders(httpPost, headers);

		StringEntity reqEntity = new StringEntity(sendData, charset);
		reqEntity.setContentType(HttpConst.Content_Type_Json);
		httpPost.setEntity(reqEntity);

		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				httpPost.abort();
				logger.error("Error status code: " + statusCode);
				return "";
			}

			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET_UTF8);
			}
			// Ensure that the entity content has been fully consumed and the underlying stream has been closed.
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			logger.error("HTTP POST error: " + url, e);
		}
		return "";
	}

	/**
	 * Add headers
	 *
	 * @param httpRequestBase HttpPost or HttpGet
	 * @param headers         headers
	 */
	private static void addHeaders(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpRequestBase.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

}
