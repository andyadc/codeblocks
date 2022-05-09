package com.andyadc.bms.modules.auth.service;

import com.andyadc.bms.modules.auth.dto.MenuDTO;
import com.andyadc.bms.modules.auth.dto.UserQuery;
import com.andyadc.bms.modules.auth.entity.AuthUser;
import com.andyadc.bms.modules.auth.mapper.AuthMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

	private AuthMapper authMapper;

	public List<MenuDTO> queryMenuListByUserId(Long userId) {
		return authMapper.selectMenuByUserId(userId);
	}

	public List<AuthUser> queryUserPage(UserQuery query) {
		query.page();
		return authMapper.selectUserPage(query);
	}

	public List<MenuDTO> queryMenuTree(Long userId) {
		List<MenuDTO> menuList = this.queryMenuListByUserId(userId);
		return tree(menuList, 0L);
	}

	private List<MenuDTO> tree(List<MenuDTO> menuList, Long parentId) {
		int size = menuList.size();
		List<MenuDTO> list = new ArrayList<>(size);
		for (MenuDTO menu : menuList) {
			if (parentId.intValue() == menu.getParentId()) {
				menu.setChildren(tree(menuList, menu.getId()));
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
