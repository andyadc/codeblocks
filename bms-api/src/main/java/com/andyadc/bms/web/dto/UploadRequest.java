package com.andyadc.bms.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class UploadRequest implements Serializable {

	private static final long serialVersionUID = 4372238315462308109L;

	private byte[] resourceBytes;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	public byte[] getResourceBytes() {
		return resourceBytes;
	}

	public void setResourceBytes(byte[] resourceBytes) {
		this.resourceBytes = resourceBytes;
	}

}
