package com.andyadc.bms.web;

import com.andyadc.bms.captcha.CaptchaDTO;
import com.andyadc.bms.captcha.CaptchaService;
import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class CaptchaController {

	private final CaptchaService captchaService;

	public CaptchaController(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	@RequestMapping("/captcha/index")
	public String index() {
		return "captcha";
	}

	@GetMapping("/captcha/gen")
	public @ResponseBody
	Response<Object> gen() {
		CaptchaDTO dto = captchaService.gen();
		return Response.success(dto);
	}

	@PostMapping("/captcha/gen")
	public @ResponseBody
	Response<Object> gen(@RequestBody CaptchaDTO captcha) {
		CaptchaDTO dto = captchaService.gen(captcha);
		return Response.success(dto);
	}

	@PostMapping("/captcha/validate")
	public @ResponseBody
	Response<Object> validate(@RequestBody CaptchaDTO captcha) {
		boolean validated = captchaService.validate(captcha);
		return validated ? Response.success() : Response.of(RespCode.VALIDATE_CODE_ERROR);
	}
}
