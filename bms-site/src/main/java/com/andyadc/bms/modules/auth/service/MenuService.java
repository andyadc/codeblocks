package com.andyadc.bms.modules.auth.service;

import com.andyadc.bms.modules.auth.dto.MenuDTO;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

	private AuthMapper authMapper;

	public List<MenuDTO> queryNavMenu(Long userId) {
		List<MenuDTO> dtoList = authMapper.selectMenuByUserId(userId);
		if (dtoList == null || dtoList.isEmpty()) {
			return new ArrayList<>();
		}

		return nav(dtoList, 0L);
	}

	private List<MenuDTO> nav(List<MenuDTO> menuList, Long parentId) {
		int size = menuList.size();
		List<MenuDTO> list = new ArrayList<>(size);
		for (MenuDTO menu : menuList) {
			if (parentId.intValue() == menu.getParentId()
				&& (menu.getType() == 0 || menu.getType() == 1)) {
				menu.setChildren(nav(menuList, menu.getId()));
				list.add(menu);
			}
		}
		return list;
	}

	@Inject
	public void setAuthMapper(AuthMapper authMapper) {
		this.authMapper = authMapper;
	}
}
