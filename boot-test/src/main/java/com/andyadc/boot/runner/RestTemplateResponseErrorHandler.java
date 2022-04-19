package com.andyadc.boot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
			|| response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			//Handle SERVER_ERROR
			logger.error("SERVER_ERROR");
			throw new HttpClientErrorException(response.getStatusCode());
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			//Handle CLIENT_ERROR
			logger.error("CLIENT_ERROR");
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new RuntimeException();
			}
		}
	}
}
