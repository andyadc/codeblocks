package com.andyadc.bms.web;

import com.andyadc.bms.common.Response;
import com.andyadc.bms.modules.auth.dto.AuthUserDTO;
import com.andyadc.bms.service.MobileService;
import com.andyadc.bms.web.dto.SmsSendRequest;
import com.andyadc.codeblocks.kit.net.IPUtil;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/mobile")
@RestController
public class MobileController {

	private MobileService mobileService;

	@Inject
	public void setSmsService(MobileService mobileService) {
		this.mobileService = mobileService;
	}

	@PostMapping("/send")
	public ResponseEntity<Object> send(@Validated @RequestBody SmsSendRequest sms, HttpServletRequest request) {
		AuthUserDTO userDTO = new AuthUserDTO();
		String ip = IPUtil.getRemoteIp(request);
		userDTO.setRequestIP(ip);
		userDTO.setPhoneNo(sms.getPhoneNo());
		mobileService.sendSms(userDTO);
		return ResponseEntity.ok(Response.success());
	}
}
