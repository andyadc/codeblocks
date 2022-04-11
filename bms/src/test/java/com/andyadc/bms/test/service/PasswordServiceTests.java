package com.andyadc.bms.test.service;

import com.andyadc.bms.security.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class PasswordServiceTests {

	@Inject
	public PasswordService passwordService;

	@Test
	public void testEncode() {
		String rawStr = "123";
		String encoded = passwordService.encode(rawStr);
		System.out.println(encoded);
		System.out.println(passwordService.getClass());
	}
}
