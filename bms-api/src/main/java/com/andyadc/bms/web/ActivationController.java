package com.andyadc.bms.web;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.service.ActivationService;
import com.andyadc.bms.service.dto.ActivationDTO;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/openapi")
@Controller
public class ActivationController {

	private static final Logger logger = LoggerFactory.getLogger(ActivationController.class);

	private ActivationService activationService;

	@RequestMapping("/regitrationConfirm")
	public ResponseEntity<Object> regitrationConfirm(HttpServletRequest request) {
		String activationCode = request.getParameter("activationCode");
		String t = request.getParameter("t");
		ActivationDTO dto = new ActivationDTO();
		dto.setActivationCode(activationCode);
		dto.setTimestamps(t);

		boolean flag = activationService.regitrationConfirm(dto);
		Response<?> response = flag ? Response.success() : Response.of(RespCode.ACTIVATION_ERROR);
		return ResponseEntity.ok(response);
	}

	@Inject
	public void setActivationService(ActivationService activationService) {
		this.activationService = activationService;
	}
}
