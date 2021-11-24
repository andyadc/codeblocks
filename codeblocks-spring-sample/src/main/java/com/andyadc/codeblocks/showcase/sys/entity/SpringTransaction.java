package com.andyadc.codeblocks.showcase.sys.entity;

import com.andyadc.codeblocks.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class SpringTransaction extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String name;

	private Integer age;

	private Long number;

	private Date createTime;

	private Date updateTime;

	private Integer version;

	public SpringTransaction() {
	}

	public SpringTransaction(String name) {
		this.name = name;
	}

	public SpringTransaction(String name, Integer age, Long number) {
		this.name = name;
		this.age = age;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
