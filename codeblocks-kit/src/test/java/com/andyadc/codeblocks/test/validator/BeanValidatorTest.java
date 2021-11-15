package com.andyadc.codeblocks.test.validator;

import com.andyadc.codeblocks.kit.validator.BeanValidator;
import org.junit.jupiter.api.Test;

/**
 * ref
 * <p>
 * https://reflectoring.io/bean-validation-with-spring-boot/
 * </p>
 */
public class BeanValidatorTest {

	@Test
	public void testValidate() {
		Bean bean1 = new Bean();
		bean1.setMobile("123");
		bean1.setName("xyz");

		Bean bean2 = new Bean();
		bean2.setMobile("13701937827");

		Bean bean3 = new Bean();
		bean3.setAge("101");

		System.out.println(BeanValidator.validateObject(bean1, bean2, bean3));
	}

	@Test
	public void testValidateIpAddress() {
		Bean bean = new Bean();
		bean.setName("adc");
		bean.setIpAddress("111.159.289.365");
		System.out.println(BeanValidator.validateObject(bean));
	}
}
