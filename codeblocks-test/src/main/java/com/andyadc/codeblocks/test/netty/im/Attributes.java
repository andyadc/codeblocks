package com.andyadc.codeblocks.test.netty.im;

import io.netty.util.AttributeKey;

/**
 * @author andy.an
 * @since 2018/10/16
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
