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
@Setter
@Getter
@ToString
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte command() {
        return Command.MESSAGE_REQUEST;
    }
}
