package com.andyadc.bms.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bms.file.storage")
public class FileStorageSettings {

	// KB
	private Long maxSize;
	private OsPath windows;
	private OsPath linux;
	private OsPath mac;

	public OsPath getPath() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			return windows;
		} else if (os.toLowerCase().startsWith("mac")) {
			return mac;
		}
		return linux;
	}

	public Long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}

	public OsPath getWindows() {
		return windows;
	}

	public void setWindows(OsPath windows) {
		this.windows = windows;
	}

	public OsPath getLinux() {
		return linux;
	}

	public void setLinux(OsPath linux) {
		this.linux = linux;
	}

	public OsPath getMac() {
		return mac;
	}

	public void setMac(OsPath mac) {
		this.mac = mac;
	}

	public static class OsPath {
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}
}
