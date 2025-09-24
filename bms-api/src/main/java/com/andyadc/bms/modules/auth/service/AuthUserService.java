package com.andyadc.bms.modules.auth.service;

import com.andyadc.bms.exception.IllegalPasswordException;
import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.modules.auth.entity.AuthMenu;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import com.andyadc.bms.modules.auth.mapper.AuthUserMapper;
import com.andyadc.bms.security.service.PasswordService;
import com.andyadc.bms.validation.PasswordConstraintValidator;
import com.andyadc.codeblocks.kit.mask.MaskType;
import com.andyadc.codeblocks.kit.text.StringUtil;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthUserService {

	private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);

	private AuthMapper authMapper;
	private AuthUserMapper userMapper;
	private PasswordService passwordService;

	public AuthUser findUserByPhoneNo(String phoneNo) {
		return userMapper.findByPhoneNo(phoneNo);
	}

	public AuthUserDTO findByPhoneNo(String phoneNo) {
		AuthUser authUser = userMapper.findByPhoneNo(phoneNo);
		if (authUser == null) {
			logger.warn("Cannot find auth user, phoneNo - [{}]", MaskType.MOBILE.mask(phoneNo));
			return null;
		}

		Long userId = authUser.getId();
		AuthUserDTO dto = new AuthUserDTO();
		dto.setId(userId);
		dto.setPhoneNo(phoneNo);
		dto.setUsername(authUser.getUsername());
		dto.setPassword(authUser.getPassword());
		dto.setStatus(authUser.getStatus());

		List<String> permissionList = queryPermissionList(userId);
		dto.setAuthorities(permissionList);
		return dto;
	}

	public AuthUserDTO findByUsername(String username) {
		AuthUser authUser = userMapper.findByUsername(username);
		if (authUser == null) {
			logger.warn("Cannot find auth user, username - [{}]", username);
			return null;
		}

		Long userId = authUser.getId();
		AuthUserDTO dto = new AuthUserDTO();
		dto.setId(userId);
		dto.setUsername(username);
		dto.setPhoneNo(authUser.getPhoneNo());
		dto.setPassword(authUser.getPassword());
		dto.setStatus(authUser.getStatus());

		List<String> permissionList = queryPermissionList(userId);
		dto.setAuthorities(permissionList);
		return dto;
	}

	private List<String> queryPermissionList(Long userId) {
		List<AuthMenu> menus = authMapper.selectMenuByUserId(userId);
		return menus.stream()
			.map(AuthMenu::getPermission)
			.filter(StringUtil::isNotBlank)
			.collect(Collectors.toList());
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

		AuthUser authUser = new AuthUser();
		String password = passwordService.encode(dto.getPassword());
		authUser.setPassword(password);
		authUser.setUsername(dto.getUsername());
		authUser.setEmail(dto.getEmail());
		authUser.setPhoneNo(dto.getPhoneNo());
		authUser.setStatus(0);
		authUser.setDeleted(0);
		authUser.setVersion(1);

		LocalDateTime now = LocalDateTime.now();
		authUser.setCreateTime(now);
		authUser.setUpdateTime(now);
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
