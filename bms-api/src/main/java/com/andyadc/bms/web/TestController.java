package com.andyadc.bms.web;

import com.andyadc.codeblocks.kit.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/test")
@Controller
public class TestController {

	@ResponseBody
	@RequestMapping(value = "/4",
		consumes = "application/x-www-form-urlencoded", produces = "application/text")
	public String test4(@RequestBody MultiValueMap<String, String> formData) {
		Map<String, String> singleValueMap = formData.toSingleValueMap();
		System.out.println(singleValueMap);
		return "success";
	}

	@ResponseBody
	@RequestMapping("/")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		ServletUtil.printHttpServletRequest(request);
		Map<String, String> params = ServletUtil.getReqParams(request);
		System.out.println(params);

		Map<String, String> headers = ServletUtil.getHeaders(request);
		System.out.println(headers);
		return "test";
	}

	@RequestMapping("/1")
	public String test1() {
		return "test";
	}

	@RequestMapping("/2")
	public String test2() {
		return "mail/verfication-code-mail";
	}

	@RequestMapping("/3")
	public String test3() {
		return "mail/verfication-code-mail";
	}
}
