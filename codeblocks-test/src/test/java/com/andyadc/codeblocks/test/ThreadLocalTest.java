package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

/**
 * @author andy.an
 * @since 2018/5/9
 */
public class ThreadLocalTest {

	private static final ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();

	@Test
	public void test() {
		stringThreadLocal.set("abc");
		integerThreadLocal.set(123);
		Thread thread = Thread.currentThread();
		System.out.println(stringThreadLocal.get());
		System.out.println(integerThreadLocal.get());
	}
}
