package com.andyadc.codeblocks.test.date;

import com.andyadc.codeblocks.kit.time.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class DateTests {

	@Test
	public void test001() {
		String date2String = DateUtil.date2String(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		System.out.println(date2String);
	}
}
