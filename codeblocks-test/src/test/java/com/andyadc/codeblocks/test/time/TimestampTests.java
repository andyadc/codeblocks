package com.andyadc.codeblocks.test.time;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.junit.jupiter.api.Test;

public class TimestampTests {

	@Test
	public void test01() {
		long startTimeMillis = System.currentTimeMillis();
		System.out.println(startTimeMillis);

		long startNanoTime = System.nanoTime();
		System.out.println(startNanoTime);

		ThreadUtil.sleep(100L);

		System.out.println((System.currentTimeMillis() - startTimeMillis));
		System.out.println((System.nanoTime() - startNanoTime) / 1000000F);
	}
}
