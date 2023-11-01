package com.andyadc.workflow.base;

import java.io.Serializable;

public class TransactionFlow implements Serializable {

	private static final long serialVersionUID = -6158552440283617474L;

	/**
	 * 功能码
	 */
	private String functionCode;

	private Long trxId;

	/**
	 * 交易类型
	 */
	private String trxType;
	/**
	 * 支付方式
	 */
	private String payMode;
	/**
	 * 支付类型，聚合支付用
	 */
	private String payType;
	/**
	 * 渠道类型
	 */
	private String channelType;

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public Long getTrxId() {
		return trxId;
	}

	public void setTrxId(Long trxId) {
		this.trxId = trxId;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
}
