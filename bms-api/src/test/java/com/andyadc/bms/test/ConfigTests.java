package com.andyadc.bms.test;

import com.andyadc.bms.modules.file.FileStorageSettings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class ConfigTests {

	@Inject
	public FileStorageSettings fileStorageSettings;

	@Value("${redis.key.prefix:}")
	private String prefix;

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
