package com.andyadc.bms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * https://codingnconcepts.com/spring-boot/spring-value-annotation/
 */
@Configuration
public class PropertiesConfig {

	@Value("#{new java.text.SimpleDateFormat('yyyy-MM-dd').parse('${bms.api.build.date}')}")
	private Date buildDate;

	@Value("#{T(java.time.LocalDate).parse('${bms.api.start.date}')}")
	private LocalDate startDate;

	@Value("#{T(java.time.LocalDateTime).parse('${bms.api.timestamp}')}")
	private LocalDateTime timestamp;

	@Value("#{systemProperties}")
	private Map<String, String> systemPropertiesMap;

	@Override
	public String toString() {
		return "PropertiesConfig{" +
			"buildDate=" + buildDate +
			", startDate=" + startDate +
			", timestamp=" + timestamp +
			", systemPropertiesMap=" + systemPropertiesMap +
			'}';
	}
}
