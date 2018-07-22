package com.andyadc.permission.entity;

import com.andyadc.codeblocks.common.BaseEntity;

import java.util.Date;

public class AuthLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private int type;
    private Long targetId;
    private int status;
    private String operator;
    private String optIp;
    private Date createdTime;
    private Date updatedTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getOptIp() {
        return optIp;
    }

    public void setOptIp(String optIp) {
        this.optIp = optIp == null ? null : optIp.trim();
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
}