package com.andyadc.scaffold.showcase.common.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 带附件请求
 *
 * @author andaicheng
 * @version 2017/3/18
 */
public class HttpFormRequestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpFormRequestUtils.class);

    private static final CloseableHttpClient httpClient;
    private static final String CHARSET_UTF8 = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    private HttpFormRequestUtils() {
    }

    public static String formRequest(String requestUrl, Map<String, String> requestParams, String fileName, String filePath) {
        String responseStr = "";
        if (StringUtils.isBlank(requestUrl)) {
            LOG.warn("requestUrl is null");
            return responseStr;
        }
        try {
            HttpPost httpPost = new HttpPost(requestUrl);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setCharset(Charset.forName(CHARSET_UTF8));
            if (StringUtils.isNotBlank(filePath)) {
                FileBody fileBody = new FileBody(new File(filePath));
                multipartEntityBuilder.addPart(StringUtils.isNotBlank(fileName) ? fileName : fileBody.getFilename(), fileBody);
                LOG.info("multipartEntityBuilder add file part name: {}", fileBody.getFilename());
            }
            if (requestParams != null && !requestParams.isEmpty()) {
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
                }
                LOG.info("multipartEntityBuilder add textBody num: {}", requestParams.size());
            }

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("response status code: {}", statusCode);
                if (statusCode != HttpStatus.SC_OK) {
                    httpPost.abort();
                    throw new RuntimeException("error status code: " + statusCode);
                }

                HttpEntity respEntity = response.getEntity();
                if (respEntity != null) {
                    responseStr = EntityUtils.toString(respEntity, CHARSET_UTF8);
                    LOG.info("response string: {}", responseStr);
                }
                EntityUtils.consume(respEntity);
            } finally {
                response.close();
            }

            return responseStr;
        } catch (Exception e) {
            LOG.error("formRequest error: " + requestUrl, e);
            throw new RuntimeException("formRequest error: " + requestUrl, e);
        }
    }
}
