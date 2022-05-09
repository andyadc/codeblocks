package com.andyadc.bms.web;

import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/")
@Controller
public class LoginController {

	@GetMapping("/login")
	public ModelAndView login(HttpServletRequest request, ModelMap model) {
		return new ModelAndView("login", model);
	}

	@PostMapping(value = "/login")
	public void login(HttpServletRequest request) {

	}

	@RequestMapping("/captcha/gen")
	public void generate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaptchaUtil.out(request, response);
	}

	@RequestMapping("/captcha/verify")
	public ResponseEntity<Object> verify(HttpServletRequest request, String captcha) {
		boolean ver = CaptchaUtil.ver(captcha, request);
		return ResponseEntity.ok(ver ? "success" : "fail");
	}
}
