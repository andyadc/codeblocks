package com.andyadc.codeblocks.test.netty.im.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author andy.an
 * @since 2018/10/16
 */
@Getter
@Setter
@ToString
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(serialize = false, deserialize = false)
    private Byte version = 1;

    /**
     * 指令
     */
    @JSONField(serialize = false)
    public abstract Byte command();
}
