package com.andyadc.codeblocks.test.time;

import com.andyadc.codeblocks.kit.time.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DateTests {

	@Test
	public void testLocalDateTimeBoundary() {
		LocalDateTime min = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		System.out.println(min); // 2020-08-27T00:00
		Date date = DateUtil.toDate(min);
		System.out.println(date); // Thu Aug 27 00:00:00 CST 2020
		System.out.println(DateUtil.date2String(date)); // 2020-08-27 00:00:00.000

		LocalDateTime max = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		System.out.println(max); // 2020-08-27T23:59:59.999999999
		date = DateUtil.toDate(max);
		System.out.println(date); // Thu Aug 27 23:59:59 CST 2020
		System.out.println(DateUtil.date2String(date)); // 2020-08-27 23:59:59.999
	}

	@Test
	public void testDateBoundary() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date time = calendar.getTime();
		System.out.println(DateUtil.date2String(time));

		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		time = calendar.getTime();
		System.out.println(DateUtil.date2String(time));
	}
}
