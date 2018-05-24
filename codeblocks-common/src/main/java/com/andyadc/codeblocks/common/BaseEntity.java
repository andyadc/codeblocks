package com.andyadc.codeblocks.common;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /* PRIMARY KEY AUTO_INCREMENT */
    private Long key;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "key=" + key +
                "}";
    }
}
