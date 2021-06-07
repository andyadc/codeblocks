package com.andyadc.codeblocks.showcase.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("pear/")
@Controller
public class PearController {

	@GetMapping("/login")
	public String login() {
		return "pear/login";
	}
}
