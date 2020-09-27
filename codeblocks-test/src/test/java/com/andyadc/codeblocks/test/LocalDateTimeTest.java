package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

/**
 * andy.an
 */
public class LocalDateTimeTest {

	@Test
	public void testLocalDateTime() {
		System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
		LocalDateTime max = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		System.out.println(max);
		System.out.println(localDateTime2Date(max));
		Duration duration = Duration.between(Instant.now(), max.toInstant(ZoneOffset.of("+8")));
		long minutes = duration.toMinutes();
		long hours = duration.toHours();
		System.out.println(minutes);
		System.out.println(hours);
	}

	@Test
	public void testLocalDate() {
		System.out.println(LocalDate.now().toString());
	}

	private Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}
}
