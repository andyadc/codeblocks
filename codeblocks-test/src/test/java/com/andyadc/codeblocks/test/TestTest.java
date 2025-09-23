package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestTest {

	@Test
	public void test008() {
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.version"));
		System.out.println(System.getProperty("os.arch"));

		System.out.println(System.getProperty("java.home"));
		System.out.println(System.getProperty("java.version"));
		System.out.println(System.getProperty("java.vendor"));
		System.out.println(System.getProperty("java.runtime.version"));

		System.out.println(System.getProperty("user.home"));
		System.out.println(System.getProperty("user.dir"));

		System.out.println(File.pathSeparator);
		System.out.println(System.lineSeparator());
		System.out.println(FileSystems.getDefault().getSeparator());
	}

	@Test
	public void test007() {
		long l1 = System.currentTimeMillis();
		long l2 = System.nanoTime();
		ThreadUtil.sleep(123L);

		System.out.println(System.currentTimeMillis() - l1);
		System.out.println((System.nanoTime() - l2) / 1000L / 1000L);

		int i = 5;
		int result = i++;
		System.out.println(i);
		System.out.println(result);

		int j = 5;
		int result2 = ++j;
		System.out.println(j);
		System.out.println(result2);

		Map<String, Object> map = new HashMap<>();
		BigDecimal fee = ((BigDecimal) map.get("PAYEEFEE"));
		System.out.println(fee);

		String url = "http://mcs-inner.99bill.com/hat-notify-gateway/notify/sendNotifyMessage&&umgwNotifyFlag";
		int idx = url.lastIndexOf("&&&");
		System.out.println(idx);
		System.out.println(url.substring(idx + 2));
	}

	@Test
	public void test006() {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis); // 1746761561876

		long epochMilli = Instant.now().toEpochMilli();
		System.out.println(epochMilli); // 1746761561881

	}

	@Test
	public void test005() {
		String path = Paths.get("").toAbsolutePath().toString();
		System.out.println(path);

	}

	@Test
	public void test004() {
		String javaVersion = System.getProperty("java.version");
		System.out.println(javaVersion);

	}

	@Test
	public void test003() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println(name);
	}

	@Test
	public void test002() {
		BigDecimal no = new BigDecimal("10");
		System.out.println(no);
		System.out.println(no.toEngineeringString());
		System.out.println(no.toPlainString());
	}

	@Test
	public void test001() {
		int days = 5;
		String platformCode = "100";
		String platDays = " 100 = 1 , 200 = 3 ";

		if (StringUtils.isNotBlank(platDays)) {
			// 100|15,200|20,300|30,
			String[] platDayArray = platDays.split(",");
			for (String platDay : platDayArray) {
				String[] platformAndDayArray = platDay.trim().split("=");
				if (platformAndDayArray.length >= 2) {
					String memberCode = platformAndDayArray[0].trim();
					if (platformCode.equals(memberCode)) {
						days = Integer.parseInt(platformAndDayArray[1].trim());
						break;
					}
				}
			}
		}

		System.out.println(days);
	}
}
