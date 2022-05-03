package com.andyadc.bms.modules.file;

import java.io.Serializable;

public class FileStorageDTO implements Serializable {

	private static final long serialVersionUID = 397326649036725577L;

	private String localStoragePath;
	private String resourcePath;

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getLocalStoragePath() {
		return localStoragePath;
	}

	public void setLocalStoragePath(String localStoragePath) {
		this.localStoragePath = localStoragePath;
	}
}
