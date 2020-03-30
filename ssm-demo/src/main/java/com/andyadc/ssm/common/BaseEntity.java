package com.andyadc.ssm.common;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/* PRIMARY KEY AUTO_INCREMENT */
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BaseEntity{" +
			"id=" + id +
			"}";
	}
}
