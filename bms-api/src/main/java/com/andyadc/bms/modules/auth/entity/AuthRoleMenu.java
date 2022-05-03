package com.andyadc.bms.modules.auth.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class AuthRoleMenu {

	@Id
	private Long id;
	private Long roleId;
	private Long menuId;

	public AuthRoleMenu() {
	}

	public AuthRoleMenu(Long roleId, Long menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
}
