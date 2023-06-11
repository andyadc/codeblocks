package com.andyadc.ssm.persistence.entity;

public class SimpleTableRecord {

	private String username;

	public SimpleTableRecord() {
	}

	public SimpleTableRecord(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
