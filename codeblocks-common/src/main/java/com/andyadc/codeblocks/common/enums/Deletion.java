package com.andyadc.codeblocks.common.enums;

/**
 * 删除状态枚举
 */
public enum Deletion {

	DELETED(1, "已删除"),
	UNDELETED(0, "未删除"),
	;

	private final int state;
	private final String desc;

	Deletion(int state, String desc) {
		this.state = state;
		this.desc = desc;
	}

	public int state() {
		return state;
	}

	public String desc() {
		return desc;
	}
}
