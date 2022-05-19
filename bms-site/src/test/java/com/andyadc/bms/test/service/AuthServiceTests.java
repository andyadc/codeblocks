package com.andyadc.bms.test.service;

import com.andyadc.bms.modules.auth.dto.MenuDTO;
import com.andyadc.bms.modules.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;

@SpringBootTest
public class AuthServiceTests {

	@Inject
	private AuthService authService;

	@Test
	public void testTree() {
		List<MenuDTO> tree = authService.queryMenuTree(7067L);
		System.out.println(tree);
	}
}
