package com.andyadc.codeblocks.test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

class Bean implements Serializable {

	private static final long serialVersionUID = 4650673407078015618L;

	private Integer id;
	private String name;
	private Long num;
	private Float height;
	private Boolean flag;
	private Date datetime = new Date();
	private LocalDateTime createTime = LocalDateTime.now();
	private List<String> hobbies = new ArrayList<>();
	private Map<String, Integer> scores = new HashMap<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public List<String> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}

	public Map<String, Integer> getScores() {
		return scores;
	}

	public void setScores(Map<String, Integer> scores) {
		this.scores = scores;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Bean.class.getSimpleName() + "[", "]")
			.add("id=" + id)
			.add("name=" + name)
			.add("num=" + num)
			.add("height=" + height)
			.add("flag=" + flag)
			.add("datetime=" + datetime)
			.add("createTime=" + createTime)
			.add("hobbies=" + hobbies)
			.add("scores=" + scores)
			.toString();
	}
}
