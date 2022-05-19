package com.andyadc.bms.test.service;

import com.andyadc.bms.modules.auth.dto.MenuDTO;
import com.andyadc.bms.modules.auth.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;

@SpringBootTest
public class MenuServiceTests {

	@Inject
	private MenuService menuService;
	@Inject
	private ObjectMapper objectMapper;

	@Test
	public void testNavMenu() throws Exception {
		List<MenuDTO> navMenu = menuService.queryNavMenu(7067L);
		System.out.println(objectMapper.writeValueAsString(navMenu));
	}
}
