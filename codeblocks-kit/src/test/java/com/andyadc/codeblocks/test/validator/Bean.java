package com.andyadc.codeblocks.test.validator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * andy.an
 */
public class Bean {
	@NotBlank
	private String name;
	@PhoneNumber(message = "手机号码不正确")
	private String mobile;
	@Min(1)
	@Max(100)
	private String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
