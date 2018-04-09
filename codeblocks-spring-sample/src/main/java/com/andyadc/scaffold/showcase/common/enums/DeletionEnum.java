package com.andyadc.scaffold.showcase.common.enums;

/**
 * 删除状态枚举
 *
 * @author andaicheng
 * @version 2017/2/23
 */
public enum DeletionEnum {
    DELETED(1, "已删除"),
    UNDELETED(0, "未删除");

    private int state;
    private String desc;

    DeletionEnum(int state, String desc) {
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
