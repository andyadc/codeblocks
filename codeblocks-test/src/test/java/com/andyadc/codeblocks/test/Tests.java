package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;

public class Tests {

	public static void main(String[] args) {
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
	}
}
