package com.andyadc.scaffold.showcase.common.dto;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2017/10/24
 */
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1506644847754195237L;

    private String respCode;
    private String respMsg;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
