package com.andyadc.bms.web.pear;

import com.andyadc.bms.modules.auth.dto.UserQuery;
import com.andyadc.bms.modules.auth.service.AuthService;
import com.andyadc.bms.web.dto.ResultTable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/pear/sys")
@Controller
public class SystemController {

	private AuthService authService;

	@Inject
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	@RequestMapping("/menu")
	public @ResponseBody
	Object menu() {
		Long userId = 7067L;
		return authService.queryMenuTree(userId);
	}

	@RequestMapping("/user/index")
	public String userIndex() {
		return "pear/system/user/index";
	}

	@RequestMapping("/user/list")
	public @ResponseBody
	Object userList() {
		UserQuery query = new UserQuery();
		ResultTable table = new ResultTable();
		table.setCode(0);
		table.setData(authService.queryUserPage(query));
		return table;
	}

	@RequestMapping("/notice")
	public @ResponseBody
	Object notice() {

		List<Map<String, Object>> result = new ArrayList<>();

		Map<String, Object> publicArray = new HashMap<>(3);
		publicArray.put("id", "1");
		publicArray.put("title", "公告");
		publicArray.put("children", null);

		Map<String, Object> privateArray = new HashMap<>(3);
		privateArray.put("id", "2");
		privateArray.put("title", "私信");
		privateArray.put("children", null);

		Map<String, Object> noticeArray = new HashMap<>(3);
		noticeArray.put("id", "3");
		noticeArray.put("title", "通知");
		noticeArray.put("children", null);

		result.add(publicArray);
		result.add(privateArray);
		result.add(noticeArray);

		return result;
	}
}
