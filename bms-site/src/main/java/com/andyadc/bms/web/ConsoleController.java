package com.andyadc.bms.web;

import com.andyadc.bms.modules.auth.dto.LoggedUser;
import com.andyadc.bms.modules.auth.dto.MenuDTO;
import com.andyadc.bms.modules.auth.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.List;

@RequestMapping("/console")
@Controller
public class ConsoleController {

	private MenuService menuService;

	@Inject
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@RequestMapping({"", "/"})
	public String console(ModelMap modelMap) {

		LoggedUser user = new LoggedUser();
		user.setNickname("旺财");
		modelMap.addAttribute("user", user);

		List<MenuDTO> menus = menuService.queryNavMenu(7067L);
		modelMap.addAttribute("menus", menus);

		return "console";
	}
}
