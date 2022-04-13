package com.andyadc.bms.config;

import com.andyadc.bms.file.FileStorageConstants;
import com.andyadc.bms.file.FileStorageSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;

@EnableWebMvc
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(CustomWebMvcConfigurer.class);

	private FileStorageSettings fileStorageSettings;

	@Inject
	public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
		this.fileStorageSettings = fileStorageSettings;
	}

	// TODO
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String path = fileStorageSettings.getPath().getPath();
		registry.addResourceHandler(FileStorageConstants.RESOURCE_PATH_PATTERNS).addResourceLocations("file:" + path);
		registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

		logger.info("ResourceHandlers added!");
	}

}
