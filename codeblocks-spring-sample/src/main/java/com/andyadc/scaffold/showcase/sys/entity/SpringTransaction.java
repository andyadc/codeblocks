package com.andyadc.scaffold.showcase.sys.entity;

import com.andyadc.scaffold.showcase.common.entity.BaseEntity;

import java.util.Date;

public class SpringTransaction extends BaseEntity {

    private static final long serialVersionUID = 7567404900254974826L;

    private String name;

    private Integer age;

    private Long number;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    public SpringTransaction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}