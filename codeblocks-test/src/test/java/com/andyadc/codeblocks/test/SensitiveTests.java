package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.test.sensitive.SensitiveStrategy;
import org.junit.jupiter.api.Test;

public class SensitiveTests {

	@Test
	public void test() {
		SensitiveStrategy strategy = SensitiveStrategy.ID_CARD;
		String s = strategy.getDesensitizer().apply("6214830216049238");
		System.out.println(s);

		System.out.println("342501199903037709".replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2"));
		System.out.println("342501199903037709".replaceAll("(\\d)", "*"));
		System.out.println("342501199903037709".replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*"));
	}
}
