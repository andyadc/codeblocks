package com.andyadc.permission.entity;

import java.io.Serializable;

public class AuthLogWithBLOBs extends AuthLog implements Serializable {

    private static final long serialVersionUID = 1L;
    private String oldValue;
    private String newValue;

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}