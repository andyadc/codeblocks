package com.andyadc.codeblocks.test.netty.im.protocol.request;

import com.andyadc.codeblocks.test.netty.im.protocol.Packet;
import com.andyadc.codeblocks.test.netty.im.protocol.command.Command;
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
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte command() {
        return Command.LOGIN_REQUEST;
    }
}
