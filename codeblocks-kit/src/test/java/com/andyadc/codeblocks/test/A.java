package com.andyadc.codeblocks.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/4/23
 */
public class A {

    private int age;
    private Long num;
    private String name;
    private Date time;
    private List<String> list;
    private Map<String, Integer> map;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "A{" +
                "age=" + age +
                ", num=" + num +
                ", name=" + name +
                ", time=" + time +
                ", list=" + list +
                ", map=" + map +
                "}";
    }
}
