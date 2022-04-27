package com.andyadc.bms.config.rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfig {

	private ResponseErrorHandler responseErrorHandler;

	@Inject
	public void setResponseErrorHandler(ResponseErrorHandler responseErrorHandler) {
		this.responseErrorHandler = responseErrorHandler;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.errorHandler(responseErrorHandler).build();
		restTemplate.setRequestFactory(this.clientHttpRequestFactory());

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new LoggingInterceptor());
		restTemplate.setInterceptors(interceptors);

		return restTemplate;
	}

	/**
	 * URLConnection
	 *
	 * @see SimpleClientHttpRequestFactory -> URLConnection
	 * @see org.springframework.http.client.HttpComponentsClientHttpRequestFactory -> HttpComponents
	 * @see org.springframework.http.client.OkHttp3ClientHttpRequestFactory -> OkHttp
	 */
	public ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(3 * 1000);
		factory.setReadTimeout(7 * 1000);

		return factory;
	}
}
