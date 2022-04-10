package com.andyadc.bms.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;

@Controller
public class IndexController {

	@RequestMapping({"/", "/index"})
	public ModelAndView index(ModelMap modelMap) {
		modelMap.addAttribute("timestamp", ZonedDateTime.now());

		return new ModelAndView("index", modelMap);
	}
}
