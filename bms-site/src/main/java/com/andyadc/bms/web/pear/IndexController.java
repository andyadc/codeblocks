package com.andyadc.bms.web.pear;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;

@RequestMapping("/pear")
public class IndexController {

	@RequestMapping({"/home"})
	public String home(ModelMap modelMap) {
		modelMap.addAttribute("timestamp", ZonedDateTime.now());
		return "pear/system/home";
	}

	@RequestMapping({"/console"})
	public String console(ModelMap modelMap) {
		modelMap.addAttribute("timestamp", ZonedDateTime.now());
		return "pear/console";
	}
}
