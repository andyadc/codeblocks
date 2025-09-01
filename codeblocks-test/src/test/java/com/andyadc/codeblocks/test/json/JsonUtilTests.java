package com.andyadc.codeblocks.test.json;

import com.andyadc.codeblocks.common.JsonUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtilTests {

	private int a;
	private Integer b;
	private long c;
	private Long d;
	private Date date;
	private String s;
	private int[] arr;
	private List<String> list;
	private Map<String, Integer> map;

    public static void main(String[] args) {
		JsonUtilTests dto = new JsonUtilTests();

		System.out.println(JsonUtils.toJSONString(dto));
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }

    public Long getD() {
        return d;
    }

    public void setD(Long d) {
        this.d = d;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
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
}
