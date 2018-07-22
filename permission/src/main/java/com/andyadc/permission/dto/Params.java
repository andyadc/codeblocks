package com.andyadc.permission.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author andaicheng
 * @since 2018/7/22
 */
public class Params {

    @NotBlank
    private String message;

    @Max(10)
    @Min(5)
    @NotNull
    private Integer num;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
