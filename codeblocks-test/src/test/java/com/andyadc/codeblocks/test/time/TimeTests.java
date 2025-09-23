package com.andyadc.codeblocks.test.time;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.jupiter.api.Test;

public class TimeTests {

	@Test
	public void test() {
		DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
		LocalDateTime localDateTime = new LocalDateTime();
		System.out.println(localDateTime.toString(formatter));
		System.out.println(localDateTime.toDateTime(DateTimeZone.UTC));
	}

}
