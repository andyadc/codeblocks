package com.andyadc.bms.test;

import com.andyadc.bms.file.FileStorageSettings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class ConfigTests {

	@Inject
	public FileStorageSettings fileStorageSettings;

	@Test
	public void testFileStorageSettings() {
		System.out.println(fileStorageSettings);
		System.out.println(fileStorageSettings.getMaxSize());
		System.out.println(fileStorageSettings.getPath().getPath());
	}
}
