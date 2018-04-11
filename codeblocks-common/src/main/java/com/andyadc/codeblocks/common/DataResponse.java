package com.andyadc.codeblocks.common;

import com.andyadc.codeblocks.common.enums.StatusCode;

/**
 * @author andy.an
 * @since 2018/3/25
 */
public class DataResponse<T> extends Response {

    private static final long serialVersionUID = 1L;
    private T data;

    public DataResponse(T t) {
        this.data = t;
    }

    public static <T> DataResponse success(T t) {
        DataResponse response = new DataResponse<>(t);
        response.setCode(StatusCode.SUCCESS.getCode());
        response.setMessage(StatusCode.SUCCESS.getMessage());
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "data=" + data +
                "} " + super.toString();
    }
}
