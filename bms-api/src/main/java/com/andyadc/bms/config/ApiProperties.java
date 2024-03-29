package com.andyadc.bms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

	private Map<String, String> application;
	private Map<String, List<String>> config;
	private Map<String, Credential> users;

	public Map<String, String> getApplication() {
		return application;
	}

	public void setApplication(Map<String, String> application) {
		this.application = application;
	}

	public Map<String, List<String>> getConfig() {
		return config;
	}

	public void setConfig(Map<String, List<String>> config) {
		this.config = config;
	}

	public Map<String, Credential> getUsers() {
		return users;
	}

	public void setUsers(Map<String, Credential> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "ApiProperties{" +
			"application=" + application +
			", config=" + config +
			", users=" + users +
			'}';
	}

	public static class Credential {

		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "Credential{" +
				"username='" + username +
				", password='" + password +
				'}';
		}
	}
}
