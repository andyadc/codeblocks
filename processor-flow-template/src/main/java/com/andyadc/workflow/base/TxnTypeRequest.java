package com.andyadc.workflow.base;

import java.io.Serializable;

public class TxnTypeRequest implements Serializable {

	private static final long serialVersionUID = -1018806811396075970L;
	/**
	 * 功能码
	 */
	private String functionCode;

	/**
	 * 渠道类型
	 */
	private String channelType;

	/**
	 * 资金通道模式
	 */
	private String orderType;

	/**
	 * 付款方式
	 */
	private String payMode;

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
}
