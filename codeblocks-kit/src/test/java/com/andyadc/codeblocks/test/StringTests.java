package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

public class StringTests {

	@Test
	public void test() {
		String email = "zhangnero@163.com";
		System.out.println("原邮箱： " + email);

		email = email.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
		System.out.println("脱敏后： " + email);

		System.out.println("---------------------------");

		String phone = "13488889999";
		System.out.println("原电话： " + phone);

		phone = phone.replaceAll("(^\\d{3})\\d.*(\\d{4})", "$1****$2");
		System.out.println("脱敏后： " + phone);
	}
}
