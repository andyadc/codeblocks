package com.andyadc.scaffold.showcase.auth.entity;


import com.andyadc.codeblocks.common.BaseEntity;
import com.andyadc.codeblocks.common.annotation.MetaData;

import java.beans.Transient;
import java.util.Date;

@MetaData(desc = "系统权限用户", tableName = "t_auth_user")
public class AuthUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String salt;

    private String mobile;

    private String email;

    private int type;

    private int status;

    private int deleted;

    private Date createdTime;

    private Date updatedTime;

    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
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
                "id=" + id +
                ", username=" + username +
                ", nickname=" + nickname +
                ", password=" + password +
                ", salt=" + salt +
                ", mobile=" + mobile +
                ", email=" + email +
                ", type=" + type +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", version=" + version +
                "} " + super.toString();
    }

    @Transient
    public String getCredentialsSalt() {
        return username + "-" + salt;
    }
}