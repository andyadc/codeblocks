package com.andyadc.test;

import com.andyadc.codeblocks.common.regex.RegularExpression;

public class Test {

	public static void main(String[] args) {
		System.out.println(RegularExpression.PHONE.validate("13701937827"));
		System.out.println(RegularExpression.PHONE.validate("137019378271"));
		System.out.println(RegularExpression.PHONE.validate("12701937827"));
		System.out.println(RegularExpression.PHONE.validate(""));
		System.out.println(RegularExpression.PHONE.validate(null));
	}
}
