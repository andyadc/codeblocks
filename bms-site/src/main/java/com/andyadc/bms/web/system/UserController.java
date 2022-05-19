package com.andyadc.bms.web.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sys")
@Controller
public class UserController {

	@RequestMapping("/user/index")
	public String user() {
		return "system/user/index";
	}
}
