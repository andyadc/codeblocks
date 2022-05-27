package com.andyadc.bms.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Controller
public class IndexController {

	static String menuJson = "[{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":1,\"menuName\":\"系统管理\",\"parentName\":null,\"parentId\":0,\"orderNum\":\"1\",\"url\":\"#\",\"target\":\"\",\"menuType\":\"M\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"\",\"icon\":\"fa fa-gear\",\"children\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":100,\"menuName\":\"用户管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"1\",\"url\":\"/system/user\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:user:view\",\"icon\":\"fa fa-user-o\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":101,\"menuName\":\"角色管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"2\",\"url\":\"/system/role\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:role:view\",\"icon\":\"fa fa-user-secret\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":102,\"menuName\":\"菜单管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"3\",\"url\":\"/system/menu\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:menu:view\",\"icon\":\"fa fa-th-list\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":103,\"menuName\":\"部门管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"4\",\"url\":\"/system/dept\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:dept:view\",\"icon\":\"fa fa-outdent\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":104,\"menuName\":\"岗位管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"5\",\"url\":\"/system/post\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:post:view\",\"icon\":\"fa fa-address-card-o\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":105,\"menuName\":\"字典管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"6\",\"url\":\"/system/dict\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:dict:view\",\"icon\":\"fa fa-bookmark-o\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":106,\"menuName\":\"参数设置\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"7\",\"url\":\"/system/config\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:config:view\",\"icon\":\"fa fa-sun-o\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":107,\"menuName\":\"通知公告\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"8\",\"url\":\"/system/notice\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"system:notice:view\",\"icon\":\"fa fa-bullhorn\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":108,\"menuName\":\"日志管理\",\"parentName\":null,\"parentId\":1,\"orderNum\":\"9\",\"url\":\"#\",\"target\":\"\",\"menuType\":\"M\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"\",\"icon\":\"fa fa-pencil-square-o\",\"children\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":500,\"menuName\":\"操作日志\",\"parentName\":null,\"parentId\":108,\"orderNum\":\"1\",\"url\":\"/monitor/operlog\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:operlog:view\",\"icon\":\"fa fa-address-book\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":501,\"menuName\":\"登录日志\",\"parentName\":null,\"parentId\":108,\"orderNum\":\"2\",\"url\":\"/monitor/logininfor\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:logininfor:view\",\"icon\":\"fa fa-file-image-o\",\"children\":[]}]}]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":2,\"menuName\":\"系统监控\",\"parentName\":null,\"parentId\":0,\"orderNum\":\"2\",\"url\":\"#\",\"target\":\"\",\"menuType\":\"M\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"\",\"icon\":\"fa fa-video-camera\",\"children\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":109,\"menuName\":\"在线用户\",\"parentName\":null,\"parentId\":2,\"orderNum\":\"1\",\"url\":\"/monitor/online\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:online:view\",\"icon\":\"fa fa-user-circle\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":110,\"menuName\":\"定时任务\",\"parentName\":null,\"parentId\":2,\"orderNum\":\"2\",\"url\":\"/monitor/job\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:job:view\",\"icon\":\"fa fa-tasks\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":111,\"menuName\":\"数据监控\",\"parentName\":null,\"parentId\":2,\"orderNum\":\"3\",\"url\":\"/monitor/data\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:data:view\",\"icon\":\"fa fa-bug\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":112,\"menuName\":\"服务监控\",\"parentName\":null,\"parentId\":2,\"orderNum\":\"4\",\"url\":\"/monitor/server\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:server:view\",\"icon\":\"fa fa-server\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":113,\"menuName\":\"缓存监控\",\"parentName\":null,\"parentId\":2,\"orderNum\":\"5\",\"url\":\"/monitor/cache\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"monitor:cache:view\",\"icon\":\"fa fa-cube\",\"children\":[]}]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":3,\"menuName\":\"系统工具\",\"parentName\":null,\"parentId\":0,\"orderNum\":\"3\",\"url\":\"#\",\"target\":\"\",\"menuType\":\"M\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"\",\"icon\":\"fa fa-bars\",\"children\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":114,\"menuName\":\"表单构建\",\"parentName\":null,\"parentId\":3,\"orderNum\":\"1\",\"url\":\"/tool/build\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"tool:build:view\",\"icon\":\"fa fa-wpforms\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":115,\"menuName\":\"代码生成\",\"parentName\":null,\"parentId\":3,\"orderNum\":\"2\",\"url\":\"/tool/gen\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"tool:gen:view\",\"icon\":\"fa fa-code\",\"children\":[]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":116,\"menuName\":\"系统接口\",\"parentName\":null,\"parentId\":3,\"orderNum\":\"3\",\"url\":\"/tool/swagger\",\"target\":\"\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"tool:swagger:view\",\"icon\":\"fa fa-gg\",\"children\":[]}]},{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"menuId\":4,\"menuName\":\"若依官网\",\"parentName\":null,\"parentId\":0,\"orderNum\":\"4\",\"url\":\"http://ruoyi.vip\",\"target\":\"menuBlank\",\"menuType\":\"C\",\"visible\":\"0\",\"isRefresh\":\"1\",\"perms\":\"\",\"icon\":\"fa fa-location-arrow\",\"children\":[]}]\n";
	static String userJson = "{\"searchValue\":null,\"createBy\":null,\"createTime\":\"2022-05-13 17:36:41\",\"updateBy\":null,\"updateTime\":null,\"remark\":\"管理员\",\"params\":{},\"userId\":1,\"deptId\":103,\"parentId\":null,\"roleId\":null,\"loginName\":\"admin\",\"userName\":\"若依\",\"userType\":\"00\",\"email\":\"ry@163.com\",\"phonenumber\":\"15888888888\",\"sex\":\"1\",\"avatar\":\"/img/profile.jpg\",\"salt\":\"111111\",\"status\":\"0\",\"delFlag\":\"0\",\"loginIp\":\"127.0.0.1\",\"loginDate\":\"2022-05-15 18:40:39\",\"pwdUpdateDate\":\"2022-05-13 17:36:41\",\"dept\":{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"deptId\":103,\"parentId\":101,\"ancestors\":\"0,100,101\",\"deptName\":\"研发部门\",\"orderNum\":1,\"leader\":\"若依\",\"phone\":null,\"email\":null,\"status\":\"0\",\"delFlag\":null,\"parentName\":null},\"roles\":[{\"searchValue\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"remark\":null,\"params\":{},\"roleId\":1,\"roleName\":\"超级管理员\",\"roleKey\":\"admin\",\"roleSort\":\"1\",\"dataScope\":\"1\",\"status\":\"0\",\"delFlag\":null,\"flag\":false,\"menuIds\":null,\"deptIds\":null,\"admin\":true}],\"roleIds\":null,\"postIds\":null,\"admin\":true}\n";

	public static void main(String[] args) {
//		List<Menu> menus = JSON.parseObject(menuJson, new TypeReference<List<Menu>>() {
//		});
//		System.out.println(menus);

		User user = JSON.parseObject(userJson, new TypeReference<User>() {
		});
		System.out.println(user);
	}

	@GetMapping("/index")
	public String index(ModelMap map, HttpServletRequest request) {

		List<Menu> menus = JSON.parseObject(menuJson, new TypeReference<List<Menu>>() {
		});

		User user = JSON.parseObject(userJson, new TypeReference<User>() {
		});

		map.addAttribute("menus", menus);
		map.addAttribute("user", user);

		map.addAttribute("sideTheme", "theme-dark");
		map.addAttribute("skinName", "skin-blue");
		map.addAttribute("footer", true);
		map.addAttribute("tagsView", true);
		map.addAttribute("mainClass", "");
		map.addAttribute("copyrightYear", "2022");
		map.addAttribute("demoEnabled", true);
		map.addAttribute("isDefaultModifyPwd", false);
		map.addAttribute("isPasswordExpired", false);
		map.addAttribute("isMobile", false);

		// 菜单导航显示风格
		String menuStyle = "default";
		String indexStyle = "index";

		// 优先Cookie配置导航菜单
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName() != null && "nav-style".equalsIgnoreCase(cookie.getName())) {
				indexStyle = cookie.getValue();
				break;
			}
		}

		return "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
	}

	@RequestMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.addAttribute("version", "1.0.0");
		return "main";
	}

	@GetMapping("/system/switchSkin")
	public String switchSkin() {
		return "skin";
	}

	// 切换菜单
	@GetMapping("/system/menuStyle/{style}")
	public void menuStyle(@PathVariable String style, HttpServletResponse response) throws Exception {
		Cookie cookie = new Cookie("nav-style", null);
		cookie.setPath("/");
		cookie.setValue(URLEncoder.encode(style, "utf-8"));
		response.addCookie(cookie);
	}

	static class Menu {

		/**
		 * 菜单ID
		 */
		private Long menuId;

		/**
		 * 菜单名称
		 */
		private String menuName;

		/**
		 * 父菜单名称
		 */
		private String parentName;

		/**
		 * 父菜单ID
		 */
		private Long parentId;

		/**
		 * 显示顺序
		 */
		private String orderNum;

		/**
		 * 菜单URL
		 */
		private String url;

		/**
		 * 打开方式（menuItem页签 menuBlank新窗口）
		 */
		private String target;

		/**
		 * 类型（M目录 C菜单 F按钮）
		 */
		private String menuType;

		/**
		 * 菜单状态（0显示 1隐藏）
		 */
		private String visible;

		/**
		 * 是否刷新（0刷新 1不刷新）
		 */
		private String isRefresh;

		/**
		 * 权限字符串
		 */
		private String perms;

		/**
		 * 菜单图标
		 */
		private String icon;

		/**
		 * 子菜单
		 */
		private List<Menu> children = new ArrayList<>();

		public Long getMenuId() {
			return menuId;
		}

		public void setMenuId(Long menuId) {
			this.menuId = menuId;
		}

		public String getMenuName() {
			return menuName;
		}

		public void setMenuName(String menuName) {
			this.menuName = menuName;
		}

		public String getParentName() {
			return parentName;
		}

		public void setParentName(String parentName) {
			this.parentName = parentName;
		}

		public Long getParentId() {
			return parentId;
		}

		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}

		public String getOrderNum() {
			return orderNum;
		}

		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public String getMenuType() {
			return menuType;
		}

		public void setMenuType(String menuType) {
			this.menuType = menuType;
		}

		public String getVisible() {
			return visible;
		}

		public void setVisible(String visible) {
			this.visible = visible;
		}

		public String getIsRefresh() {
			return isRefresh;
		}

		public void setIsRefresh(String isRefresh) {
			this.isRefresh = isRefresh;
		}

		public String getPerms() {
			return perms;
		}

		public void setPerms(String perms) {
			this.perms = perms;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public List<Menu> getChildren() {
			return children;
		}

		public void setChildren(List<Menu> children) {
			this.children = children;
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", Menu.class.getSimpleName() + "[", "]")
				.add("menuId=" + menuId)
				.add("menuName=" + menuName)
				.add("parentName=" + parentName)
				.add("parentId=" + parentId)
				.add("orderNum=" + orderNum)
				.add("url=" + url)
				.add("target=" + target)
				.add("menuType=" + menuType)
				.add("visible=" + visible)
				.add("isRefresh=" + isRefresh)
				.add("perms=" + perms)
				.add("icon=" + icon)
				.add("children=" + children)
				.toString();
		}
	}

	static class User {

		private Long userId;

		private Long deptId;

		private Long parentId;

		private Long roleId;

		private String loginName;

		private String userName;

		private String userType;

		private String email;

		private String phonenumber;

		private String sex;

		private String avatar;

		private String password;

		private String salt;

		private String status;

		private String delFlag;

		private String loginIp;

		private Date loginDate;

		private Date pwdUpdateDate;

		private Long[] roleIds;

		private Long[] postIds;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getDeptId() {
			return deptId;
		}

		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}

		public Long getParentId() {
			return parentId;
		}

		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}

		public Long getRoleId() {
			return roleId;
		}

		public void setRoleId(Long roleId) {
			this.roleId = roleId;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhonenumber() {
			return phonenumber;
		}

		public void setPhonenumber(String phonenumber) {
			this.phonenumber = phonenumber;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getDelFlag() {
			return delFlag;
		}

		public void setDelFlag(String delFlag) {
			this.delFlag = delFlag;
		}

		public String getLoginIp() {
			return loginIp;
		}

		public void setLoginIp(String loginIp) {
			this.loginIp = loginIp;
		}

		public Date getLoginDate() {
			return loginDate;
		}

		public void setLoginDate(Date loginDate) {
			this.loginDate = loginDate;
		}

		public Date getPwdUpdateDate() {
			return pwdUpdateDate;
		}

		public void setPwdUpdateDate(Date pwdUpdateDate) {
			this.pwdUpdateDate = pwdUpdateDate;
		}

		public Long[] getRoleIds() {
			return roleIds;
		}

		public void setRoleIds(Long[] roleIds) {
			this.roleIds = roleIds;
		}

		public Long[] getPostIds() {
			return postIds;
		}

		public void setPostIds(Long[] postIds) {
			this.postIds = postIds;
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
				.add("userId=" + userId)
				.add("deptId=" + deptId)
				.add("parentId=" + parentId)
				.add("roleId=" + roleId)
				.add("loginName=" + loginName)
				.add("userName=" + userName)
				.add("userType=" + userType)
				.add("email=" + email)
				.add("phonenumber=" + phonenumber)
				.add("sex=" + sex)
				.add("avatar=" + avatar)
				.add("password=" + password)
				.add("salt=" + salt)
				.add("status=" + status)
				.add("delFlag=" + delFlag)
				.add("loginIp=" + loginIp)
				.add("loginDate=" + loginDate)
				.add("pwdUpdateDate=" + pwdUpdateDate)
				.add("roleIds=" + Arrays.toString(roleIds))
				.add("postIds=" + Arrays.toString(postIds))
				.toString();
		}
	}
}
