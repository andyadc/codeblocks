package com.andyadc.boot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfig {

	private ResponseErrorHandler responseErrorHandler;

	@Autowired
	public void setResponseErrorHandler(ResponseErrorHandler responseErrorHandler) {
		this.responseErrorHandler = responseErrorHandler;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder
			.errorHandler(responseErrorHandler)
			.build();

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new LoggingInterceptor());
		restTemplate.setInterceptors(interceptors);

		restTemplate.setRequestFactory(this.clientHttpRequestFactory());

		return restTemplate;
	}

	/**
	 * URLConnection
	 */
	public ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory simplefactory = new SimpleClientHttpRequestFactory();
		simplefactory.setConnectTimeout(3 * 1000);
		simplefactory.setReadTimeout(7 * 1000);

		// the use of this factory involves a performance drawback
		ClientHttpRequestFactory factory =
			new BufferingClientHttpRequestFactory(simplefactory);

		return factory;
	}
}
