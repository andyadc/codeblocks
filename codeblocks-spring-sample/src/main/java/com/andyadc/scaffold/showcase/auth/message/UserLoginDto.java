package com.andyadc.scaffold.showcase.auth.message;

import java.io.Serializable;
import java.util.Date;

/**
 * @author andy.an
 * @since 2017/8/16
 */
public class UserLoginDto implements Serializable {

    private Long userId;
    private String account;
    private Date loginTime;
    private String errorMsg;

    public UserLoginDto() {
        this.loginTime = new Date();
        this.errorMsg = "";
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
