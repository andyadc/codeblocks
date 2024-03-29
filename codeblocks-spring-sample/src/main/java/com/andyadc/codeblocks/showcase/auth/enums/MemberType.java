package com.andyadc.codeblocks.showcase.auth.enums;

public enum MemberType {

	SYS(1, 10, "系统用户"),
	USER(2, 20, "个人用户"),
	BUSINESS(3, 30, "商户");

	private final int type;
	private final int prefix;
	private final String desc;

	MemberType(int type, int prefix, String desc) {
		this.type = type;
		this.prefix = prefix;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

    public int getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }
}
