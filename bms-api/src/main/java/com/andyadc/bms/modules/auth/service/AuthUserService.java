package com.andyadc.bms.modules.auth.service;

import com.andyadc.bms.exception.IllegalPasswordException;
import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.modules.auth.entity.AuthMenu;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import com.andyadc.bms.modules.auth.mapper.AuthUserMapper;
import com.andyadc.bms.security.PasswordService;
import com.andyadc.bms.validation.PasswordConstraintValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthUserService {

	private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);

	private AuthMapper authMapper;
	private AuthUserMapper userMapper;
	private PasswordService passwordService;

	public AuthUserDTO findByUsername(String username) {
		AuthUser authUser = userMapper.findByUsername(username);
		if (authUser == null) {
			logger.error("Cannot find auth user, username - [{}]", username);
			return null;
		}

		AuthUserDTO dto = new AuthUserDTO();
		List<AuthMenu> menus = authMapper.selectMenuByUserId(authUser.getId());
		List<String> permissionList = menus.stream().map(AuthMenu::getPermission).collect(Collectors.toList());
		dto.setId(authUser.getId());
		dto.setUsername(username);
		dto.setPassword(authUser.getPassword());
		dto.setAuthorities(permissionList);
		return dto;
	}

	public AuthUser register(AuthUserDTO dto) {
		Assert.notNull(dto, "Illegal register info. AuthUserDTO is null");

		if (!dto.passwordMatch()) {
			logger.warn("Passwords do NOT match!");
			throw new IllegalPasswordException("Illegal password");
		}

		boolean valid = PasswordConstraintValidator.isValid(dto.getPassword(), dto.getUsername());
		if (!valid) {
			logger.warn("The password does not meet the password policy requirements.");
			throw new IllegalPasswordException("Illegal password");
		}

		String password = passwordService.encode(dto.getPassword());
		AuthUser authUser = new AuthUser();
		authUser.setPassword(password);
		authUser.setUsername(dto.getUsername());
		authUser.setEmail(dto.getEmail());
		authUser.setStatus(0);
		authUser.setDeleted(0);
		authUser.setVersion(1);
		authUser.setCreateTime(LocalDateTime.now());
		authUser.setUpdateTime(LocalDateTime.now());
		int result = authMapper.insertUserSelective(authUser);
		return authUser;
	}

	@Inject
	public void setAuthMapper(AuthMapper authMapper) {
		this.authMapper = authMapper;
	}

	@Inject
	public void setUserMapper(AuthUserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Inject
	public void setPasswordService(PasswordService passwordService) {
		this.passwordService = passwordService;
	}
}
