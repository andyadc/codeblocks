package com.andyadc.test.mybatis.page;

import org.junit.jupiter.api.Test;
import org.springframework.util.PatternMatchUtils;

public class PatternMatchUtilsTests {

	@Test
	public void test() {
		String id = "com.andyadc.codeblocks.mybatis.mapper.selectPagingwww";
		String regex = "*.selectPaging";
		System.out.println(PatternMatchUtils.simpleMatch(regex, id));
		System.out.println(com.andyadc.codeblocks.framework.mybatis.util.PatternMatchUtils.simpleMatch(regex, id));
	}
}
