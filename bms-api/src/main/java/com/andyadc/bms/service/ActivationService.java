package com.andyadc.bms.service;

import com.andyadc.bms.modules.auth.service.AuthUserService;
import com.andyadc.bms.service.dto.ActivationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ActivationService {

	private static final Logger logger = LoggerFactory.getLogger(ActivationService.class);

	private AuthUserService authUserService;

	public boolean regitrationConfirm(ActivationDTO dto) {
		logger.info("regitrationConfirm {}", dto);

		return false;
	}

	@Inject
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}
}
