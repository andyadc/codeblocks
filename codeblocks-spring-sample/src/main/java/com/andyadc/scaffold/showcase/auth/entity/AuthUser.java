package com.andyadc.scaffold.showcase.auth.entity;


import com.andyadc.scaffold.showcase.common.annotation.MetaData;
import com.andyadc.scaffold.showcase.common.entity.BaseEntity;

import java.util.Date;

@MetaData(desc = "系统权限用户", tableName = "t_auth_user")
public class AuthUser extends BaseEntity {

    private static final long serialVersionUID = -7830790559829715043L;

    private String identifier;

    private String name;

    private String account;

    private String password;

    private String salt;

    private String phone;

    private String email;

    private int state;

    private int isDeleted;

    private Date createTime;

    private Date updateTime;

    private int version;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier == null ? null : identifier.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCredentialsSalt() {
        return account + salt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "identifier=" + identifier +
                ", name=" + name +
                ", account=" + account +
                ", password=" + password +
                ", salt=" + salt +
                ", phone=" + phone +
                ", email=" + email +
                ", state=" + state +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                "} " + super.toString();
    }
}