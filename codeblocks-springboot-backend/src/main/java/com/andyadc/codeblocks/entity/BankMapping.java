package com.andyadc.codeblocks.entity;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class BankMapping {

    private Long id;
    private String bankCode;
    private String bankName;
    private Integer cardType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "BankMapping{" +
                "id=" + id +
                ", bankCode=" + bankCode +
                ", bankName=" + bankName +
                ", cardType=" + cardType +
                "}";
    }
}
