package com.andyadc.bms.modules.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "AuthRoleMenu")
@Table(name = "auth_role_menu")
public class AuthRoleMenu {

	@Id
	private Long id;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "menu_id")
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
