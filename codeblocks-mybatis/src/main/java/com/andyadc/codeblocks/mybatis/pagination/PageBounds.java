package com.andyadc.codeblocks.mybatis.pagination;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class PageBounds extends RowBounds implements Serializable {
    private static final long serialVersionUID = 1L;

    public PageBounds() {
    }

    public PageBounds(RowBounds rowBounds) {

    }
}
