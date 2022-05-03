package com.andyadc.bms.test;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesTests {

	@Test
	public void testAppProps() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader()
			.getResourceAsStream("META-INF/app.properties");

		Properties properties = new Properties();
		properties.load(inputStream);
		System.out.println(properties.getProperty("app.id"));
		System.out.println(properties.getProperty("app.version"));
	}
}
