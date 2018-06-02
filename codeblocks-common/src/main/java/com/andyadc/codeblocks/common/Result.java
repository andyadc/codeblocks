package com.andyadc.codeblocks.common;

import java.io.Serializable;

/**
 * @author andaicheng
 * @since 2018/6/2
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T data;
    private boolean isSuccess;


}
