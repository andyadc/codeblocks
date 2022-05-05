package com.andyadc.bms.security;

import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.modules.auth.service.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	private AuthUserService authUserService;

	public Optional<AuthUserDTO> findByUsername(String username) {
		Objects.requireNonNull(username);

		AuthUserDTO dto = authUserService.findByUsername(username);

		return Optional.ofNullable(dto);
	}

	public Optional<AuthUserDTO> findByPhoneNo(String phoneNo) {
		Objects.requireNonNull(phoneNo);

		AuthUserDTO dto = authUserService.findByPhoneNo(phoneNo);

		return Optional.ofNullable(dto);
	}

	@Inject
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}
}
