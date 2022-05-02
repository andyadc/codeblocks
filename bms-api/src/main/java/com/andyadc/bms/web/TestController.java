package com.andyadc.bms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
@Controller
public class TestController {

	@RequestMapping("/1")
	public String test1() {
		return "test";
	}

	@RequestMapping("/2")
	public String test2() {
		return "mail/verfication-code-mail";
	}
}
