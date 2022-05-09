package com.andyadc.bms.test;

import com.andyadc.bms.modules.auth.dto.UserQuery;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;

@SpringBootTest
public class MapperTests {

	@Inject
	private AuthMapper authMapper;
	@Inject
	private ObjectMapper objectMapper;

	@Test
	public void testUserPage() throws Exception {
		UserQuery query = new UserQuery();
		query.setType(1);
		query.setPage(1);
		query.setLimit(2);
		query.page();
		List<AuthUser> list = authMapper.selectUserPage(query);
		System.out.println(objectMapper.writeValueAsString(list));
	}
}
