package com.andyadc.workflow.base;

public enum BizTypes {

	ORDER("ORDER", "下单"),

	PAY("PAY", "支付"),

	REFUND("REFUND", "退款"),
	;

	/**
	 * 业务类型编码
	 */
	private final String code;

	/**
	 * 业务类型描述
	 */
	private final String desc;

	BizTypes(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
