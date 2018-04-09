package com.andyadc.scaffold.showcase.auth.enums;

/**
 * @author andaicheng
 * @version 2017/2/23
 */
public enum AuthUserState {
    INIT(0, "初始化"),
    NORMAL(1, "正常"),
    BLOCKED(2, "锁定"),
    UNNORMAL(3, "异常");

    private int state;
    private String desc;

    AuthUserState(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }
}
