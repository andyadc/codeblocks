package com.andyadc.bms.test.service;

import com.andyadc.bms.auth.dto.AuthUserDTO;
import com.andyadc.bms.auth.service.AuthUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

@SpringBootTest
public class ServiceTests {

	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private AuthUserService authUserService;

	@Test
	public void testAuthUser() throws Exception {
		AuthUserDTO userDTO = authUserService.findByUsername("twwx62u");
		System.out.println(objectMapper.writeValueAsString(userDTO));
	}

}
