package com.andyadc.codeblocks.test.db.oracle;

import java.math.BigDecimal;

public class BalanceOutput {

	private String entryCode;
	private String creationDate;
	private String postDate;
	private String dealId;
	private String voucherCode;
	private String crdr;
	private BigDecimal value;
	//	private BigDecimal balance;
	private String orderCode;
	private String sequenceId;
	private String name;
	private BigDecimal payeeFee;
	private BigDecimal payerFee;
	private String orderId;
	private String transactionDate;
	private String withdrawBatchId;
	private String withdrawBankName;
	private String withdrawBankCard;

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getCrdr() {
		return crdr;
	}

	public void setCrdr(String crdr) {
		this.crdr = crdr;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

//	public BigDecimal getBalance() {
//		return balance;
//	}
//
//	public void setBalance(BigDecimal balance) {
//		this.balance = balance;
//	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPayeeFee() {
		return payeeFee;
	}

	public void setPayeeFee(BigDecimal payeeFee) {
		this.payeeFee = payeeFee;
	}

	public BigDecimal getPayerFee() {
		return payerFee;
	}

	public void setPayerFee(BigDecimal payerFee) {
		this.payerFee = payerFee;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getWithdrawBatchId() {
		return withdrawBatchId;
	}

	public void setWithdrawBatchId(String withdrawBatchId) {
		this.withdrawBatchId = withdrawBatchId;
	}

	public String getWithdrawBankName() {
		return withdrawBankName;
	}

	public void setWithdrawBankName(String withdrawBankName) {
		this.withdrawBankName = withdrawBankName;
	}

	public String getWithdrawBankCard() {
		return withdrawBankCard;
	}

	public void setWithdrawBankCard(String withdrawBankCard) {
		this.withdrawBankCard = withdrawBankCard;
	}
}
