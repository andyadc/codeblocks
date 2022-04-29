package com.andyadc.bms.service.dto;

import java.util.StringJoiner;

public class ActivationDTO {

	private String activationCode;
	private String timestamps;

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(String timestamps) {
		this.timestamps = timestamps;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ActivationDTO.class.getSimpleName() + "[", "]")
			.add("activationCode=" + activationCode)
			.add("timestamps=" + timestamps)
			.toString();
	}
}
