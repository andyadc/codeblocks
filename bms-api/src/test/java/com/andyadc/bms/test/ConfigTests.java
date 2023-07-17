package com.andyadc.bms.test;

import com.andyadc.bms.config.ApiProperties;
import com.andyadc.bms.config.PropertiesConfig;
import com.andyadc.bms.modules.file.FileStorageSettings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class ConfigTests {

	@Inject
	public FileStorageSettings fileStorageSettings;

	@Value("${redis.key.prefix:}")
	private String prefix;

	@Inject
	private PropertiesConfig propertiesConfig;

	@Autowired
	private ApiProperties apiProperties;

	@Test
	public void testProperties() {
		System.out.println(propertiesConfig);
		System.out.println(apiProperties);
	}

	@Test
	public void testFileStorageSettings() {
		System.out.println(fileStorageSettings);
		System.out.println(fileStorageSettings.getMaxSize());
		System.out.println(fileStorageSettings.getPath().getPath());
	}

	@Test
	public void testValue() {
		System.out.println(prefix);
	}
}
