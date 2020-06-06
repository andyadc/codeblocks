package com.andyadc.ssm.controller;

import com.andyadc.ssm.persistence.entity.Demo;
import com.andyadc.ssm.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@RequestMapping("/demo")
@Controller
public class DemoController {

	private final DemoService demoService;

	public DemoController(DemoService demoService) {
		this.demoService = demoService;
	}

	@ResponseBody
	@RequestMapping("/add")
	public String add() {
		Demo demo = new Demo();
		demo.setName(UUID.randomUUID().toString());
		demoService.add(demo);
		return "success";
	}
}
