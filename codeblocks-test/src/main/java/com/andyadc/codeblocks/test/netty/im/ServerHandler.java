package com.andyadc.codeblocks.test.netty.im;

import com.andyadc.codeblocks.test.netty.im.protocol.Packet;
import com.andyadc.codeblocks.test.netty.im.protocol.PacketCodeC;
import com.andyadc.codeblocks.test.netty.im.protocol.request.LoginRequestPacket;
import com.andyadc.codeblocks.test.netty.im.protocol.request.MessageRequestPacket;
import com.andyadc.codeblocks.test.netty.im.protocol.response.LoginResponsePacket;
import com.andyadc.codeblocks.test.netty.im.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * @author andy.an
 * @since 2018/10/16
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.INSTANCE().decode(requestByteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(loginRequestPacket.getVersion());

            // 登录校验
            if (valid(loginRequestPacket)) {
                // 校验成功
                loginResponsePacket.setSuccess(true);
                loginResponsePacket.setToken(UUID.randomUUID().toString());
                System.out.println(new Date() + ": 登录成功!");
            } else {
                // 校验失败
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败!");
            }

            ByteBuf responseByteBuf = PacketCodeC.INSTANCE().encode(ctx.alloc(), loginResponsePacket);
            ctx.writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE().encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        System.out.println(loginRequestPacket);
        return true;
    }
}
