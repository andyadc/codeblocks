package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class StringUtilTests {

	@Test
	public void testStrCut() {
		String str = "132h34nsdir";
//		System.out.println(str.substring(0, 300));
//		System.out.println(str.substring(0));
//		System.out.println(str.subSequence(0, 300));

		System.out.println(StringUtils.substring(str, 0, 3));
		System.out.println(StringUtils.substring(str, 0, 300));

		System.out.println(StringUtil.substring(str, 0, 3));
		System.out.println(StringUtil.substring(str, 0, 300));
	}
}
