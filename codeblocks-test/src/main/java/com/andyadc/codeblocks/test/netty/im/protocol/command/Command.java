package com.andyadc.codeblocks.test.netty.im.protocol.command;

/**
 * @author andy.an
 * @since 2018/10/16
 */
public interface Command {

    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = 2;
    Byte MESSAGE_REQUEST = 3;
    Byte MESSAGE_RESPONSE = 4;
}
