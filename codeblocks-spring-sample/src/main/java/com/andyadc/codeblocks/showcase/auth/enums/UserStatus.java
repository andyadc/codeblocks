package com.andyadc.codeblocks.showcase.auth.enums;

/**
 * @author andaicheng
 * @version 2017/2/23
 */
public enum UserStatus {
    INIT(0, "初始化"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    UNNORMAL(3, "异常");

    private int status;
    private String desc;

    UserStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
