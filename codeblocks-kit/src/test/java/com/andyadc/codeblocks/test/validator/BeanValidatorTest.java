package com.andyadc.codeblocks.test.validator;

import com.andyadc.codeblocks.kit.validator.BeanValidator;

/**
 * andy.an
 */
public class BeanValidatorTest {

	public static void main(String[] args) {
		Bean bean1 = new Bean();
		bean1.setMobile("123");
		bean1.setName("xyz");

		Bean bean2 = new Bean();
		bean2.setMobile("13701937827");

		Bean bean3 = new Bean();
		bean3.setAge("101");

		System.out.println(BeanValidator.validateObject(bean1, bean2, bean3));
	}
}
