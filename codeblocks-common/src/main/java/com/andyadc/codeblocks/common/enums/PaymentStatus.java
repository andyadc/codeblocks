package com.andyadc.codeblocks.common.enums;

/**
 * @author andy.an
 * @since 2018/5/28
 */
public enum PaymentStatus {

	INIT(0, "Init", "初始化"),
	PAID(1, "Paid", "已支付"),
	PAYING(2, "Paying", "支付中"),
	UNPAID(3, "Unpaid", "未支付"),
	CANCELLED(4, "Cancelled", "取消"),
	REFUNDING(5, "Refunding", "退款中"),
	REFUNDED(6, "Refunded", "已退款"),
	;

	int num;
	String code;
	String description;

	PaymentStatus(int num, String code, String description) {
		this.num = num;
		this.code = code;
		this.description = description;
	}
}
