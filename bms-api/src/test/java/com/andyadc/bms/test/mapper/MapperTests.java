package com.andyadc.bms.test.mapper;

import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import com.andyadc.bms.modules.auth.mapper.AuthUserMapper;
import com.andyadc.bms.modules.common.NativeMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
public class MapperTests {

	@Inject
	public AuthUserMapper authUserMapper;
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private AuthMapper authMapper;
	@Inject
	private NativeMapper nativeMapper;

	@BeforeEach
	public void before() {
		System.out.println("\r\n <<<<<<<<<<<>>>>>>>>>>> \r\n");
	}

	@AfterEach
	public void after() {
		System.out.println("\r\n <<<<<<<<<<<>>>>>>>>>>> \r\n");
	}

	@Test
	public void testNativeMapper() {
		List<LinkedHashMap<String, Object>> list
			= nativeMapper.select("select * from auth_user");
		for (LinkedHashMap<String, Object> map : list) {
			System.out.println(map);
		}

		Long result = nativeMapper.update("delete from auth_user where username = 'adc1'");
		System.out.println(result);
	}

	@Test
	public void testInsertUpdateUser() {
		AuthUser user = new AuthUser();
		user.setUsername("qq");
		user.setPassword("rtegrfd");
		user.setStatus(1);
		user.setDeleted(0);
		user.setType(1);
		user.setCreateTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		authMapper.insertUserSelective(user);
	}

	@Test
	public void testAuthMapper() {
		authMapper.selectMenuByUserId(1L).forEach((m) -> {
			try {
				System.out.println(objectMapper.writeValueAsString(m));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void testAuthUserMapper() throws Exception {
		AuthUser user = authUserMapper.findByUsername("adc");
		System.out.println(objectMapper.writeValueAsString(user));

		user = authUserMapper.findByPhoneNo("321");
		System.out.println(objectMapper.writeValueAsString(user));
	}
}
