package com.andyadc.bms.test;

import com.andyadc.bms.captcha.CaptchaDTO;
import com.andyadc.bms.captcha.CaptchaService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class CaptchaServiceTests {

	@Inject
	private CaptchaService captchaService;

	@Test
	public void testGen() {
		CaptchaDTO dto = captchaService.gen();
		System.out.println(dto);
		System.out.println(captchaService.validate(dto));
	}
}
