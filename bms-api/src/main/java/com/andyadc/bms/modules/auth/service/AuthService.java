package com.andyadc.bms.modules.auth.service;

import com.andyadc.bms.modules.auth.entity.AuthMenu;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

	private AuthMapper authMapper;

	private static List<AuthMenu> tree(List<AuthMenu> menuList, Long parentId) {
		int size = menuList.size();
		List<AuthMenu> list = new ArrayList<>(size);
		for (AuthMenu menu : menuList) {
			long pid = menu.getParentId();
			if (parentId == pid) {
				menu.setChildMenu(tree(menuList, pid));
				list.add(menu);
			}
		}
		return list;
	}

	public List<AuthMenu> queryMenuListByUserId(Long userId) {
		return authMapper.selectMenuByUserId(userId);
	}

	public List<AuthMenu> queryMenuTree(Long userId) {
		List<AuthMenu> menuList = this.queryMenuListByUserId(userId);
		return tree(menuList, 0L);
	}

	@Inject
	public void setAuthMapper(AuthMapper authMapper) {
		this.authMapper = authMapper;
	}
}
