package com.andyadc.codeblocks.showcase.auth.entity;


import com.andyadc.codeblocks.common.BaseEntity;
import com.andyadc.codeblocks.common.annotation.MetaData;

import java.beans.Transient;
import java.util.Date;

@MetaData(desc = "系统权限用户", tableName = "t_auth_user")
public class AuthUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String username;

    private String nickname;

    private String password;

    private String salt;

    private String mobile;

    private String email;

    private Integer type;

    private Integer status;

    private Integer deleted;

    private Date createTime;

    private Date updateTime;

    private int version;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
                "username=" + username +
                ", nickname=" + nickname +
                ", password=" + password +
                ", salt=" + salt +
                ", mobile=" + mobile +
                ", email=" + email +
                ", type=" + type +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                "} " + super.toString();
    }

    @Transient
    public String getCredentialsSalt() {
        return username + "-" + salt;
    }
}