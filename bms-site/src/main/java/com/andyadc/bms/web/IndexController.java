package com.andyadc.bms.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;

@RequestMapping("/")
@Controller
public class IndexController {

	@RequestMapping({"/", "/index"})
	public String index(ModelMap modelMap) {
		modelMap.addAttribute("timestamp", ZonedDateTime.now());
		return "index";
	}
}
