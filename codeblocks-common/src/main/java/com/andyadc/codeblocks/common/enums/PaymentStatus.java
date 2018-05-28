package com.andyadc.codeblocks.common.enums;

/**
 * @author andy.an
 * @since 2018/5/28
 */
public enum PaymentStatus {

    PAID(1, "Paid", "已支付"),
    UNPAID(2, "Unpaid", "未支付"),
    CANCELLED(3, "Cancelled", "取消"),
    REFUNDED(4, "Refunded", "已退款");

    int num;
    String code;
    String description;

    PaymentStatus(int num, String code, String description) {
        this.num = num;
        this.code = code;
        this.description = description;
    }
}
