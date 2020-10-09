package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

public class UnicodeTests {

	/**
	 * java编译器会处理unicode字符，\u000d以及\u000a 正好对应“\r”回车、“\n”换行
	 * 经过编译器处理后，等效于下面的代码:
	 * <code>
	 * public void testUnicode() {
	 * String a = "Hello";
	 * //
	 * a="world";
	 * System.out.println(a);
	 * //
	 * a="hello world!";
	 * System.out.println(a);
	 * }
	 * </code>
	 */
	@Test
	public void testUnicode() {
		String a = "Hello";
		// \u000d a="world";
		System.out.println(a);
		// \u000a a="hello world!";
		System.out.println(a);
	}
}
