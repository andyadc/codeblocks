package com.andyadc.codeblocks.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/8/15
 */
public class User {

    private Long id;
    private String name;
    private int num;
    private Date birthday;
    private List<String> hobbies;
    private Map<String, Object> goods;

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
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public Map<String, Object> getGoods() {
        return goods;
    }

    public void setGoods(Map<String, Object> goods) {
        this.goods = goods;
    }
}
