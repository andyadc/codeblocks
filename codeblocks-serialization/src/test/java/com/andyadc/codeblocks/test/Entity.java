package com.andyadc.codeblocks.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author andaicheng
 * @since 2018/4/6
 */
public class Entity {

	private Integer id;
	private String name;
	private Long num;
	private Float height;
	private Boolean flag;
	private Date datetime;
	private List<String> hobbies;
	private Map<String, Integer> scores;

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
}
