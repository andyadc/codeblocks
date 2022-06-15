package com.andyadc.bms.config;

import com.andyadc.bms.management.interceptor.RequestInterceptor;
import com.andyadc.bms.management.resolver.HeaderVersionArgumentResolver;
import com.andyadc.bms.modules.file.FileStorageConstants;
import com.andyadc.bms.modules.file.FileStorageSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.util.List;

@EnableWebMvc
@Configuration
public class CustomMvcConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(CustomMvcConfig.class);

	private ObjectMapper objectMapper;
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private FileStorageSettings fileStorageSettings;
	private RequestInterceptor requestInterceptor;
	private HeaderVersionArgumentResolver headerVersionArgumentResolver;

	@Inject
	public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
		this.fileStorageSettings = fileStorageSettings;
	}

	@Inject
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Inject
	public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
		this.requestInterceptor = requestInterceptor;
	}

	@Inject
	public void setHeaderVersionArgumentResolver(HeaderVersionArgumentResolver headerVersionArgumentResolver) {
		this.headerVersionArgumentResolver = headerVersionArgumentResolver;
	}

	@Named("mvcTaskExecutor")
	@Inject
	public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	// ---------------------------------------------------------------------------------

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(headerVersionArgumentResolver);
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(threadPoolTaskExecutor);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
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
		// json
		MappingJackson2HttpMessageConverter jsonConverter = this.createJsonHttpMessageConverter();

		// xml
		HttpMessageConverter<?> xmlConverter = this.createXmlHttpMessageConverter();

		converters.add(jsonConverter);
		converters.add(xmlConverter);
	}

	private HttpMessageConverter<Object> createXmlHttpMessageConverter() {
		MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter();
		xmlConverter.setDefaultCharset(StandardCharsets.UTF_8);

		return xmlConverter;
	}

	private MappingJackson2HttpMessageConverter createJsonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		converter.setObjectMapper(objectMapper);
		return converter;
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
