package com.andyadc.bms.modules.auth.dto;

import java.util.List;
import java.util.StringJoiner;

public class MenuDTO {

	private Long id;
	private Long parentId;
	private Integer type;
	private String name;
	private String code;
	private String permission;
	private String component;
	private String path;
	private String icon;
	private String target;
	private Integer sort;
	private Integer visible;
	private Integer status;

	private List<MenuDTO> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<MenuDTO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuDTO> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MenuDTO.class.getSimpleName() + "[", "]")
			.add("id=" + id)
			.add("parentId=" + parentId)
			.add("type=" + type)
			.add("name='" + name)
			.add("code='" + code)
			.add("permission='" + permission)
			.add("component='" + component)
			.add("path='" + path)
			.add("icon='" + icon)
			.add("target='" + target)
			.add("sort=" + sort)
			.add("visible=" + visible)
			.add("status=" + status)
			.add("children=" + children)
			.toString();
	}
}
