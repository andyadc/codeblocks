package com.andyadc.bms.config;

import com.andyadc.bms.file.FileStorageConstants;
import com.andyadc.bms.file.FileStorageSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;

@EnableWebMvc
@Configuration
public class CustomMvcConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(CustomMvcConfig.class);

	private FileStorageSettings fileStorageSettings;
	private ObjectMapper objectMapper;

	@Inject
	public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
		this.fileStorageSettings = fileStorageSettings;
	}

	@Inject
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

		String path = fileStorageSettings.getPath().getPath();
		registry.addResourceHandler(FileStorageConstants.RESOURCE_PATH_PATTERNS).addResourceLocations("file:" + path);

		logger.info("ResourceHandlers added!");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		converter.setObjectMapper(objectMapper);

		converters.add(converter);
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(5242880L);
		return multipartResolver;
	}

	@Bean
	@ConditionalOnMissingBean(RequestContextListener.class)
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
