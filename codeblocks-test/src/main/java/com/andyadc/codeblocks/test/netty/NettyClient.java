package com.andyadc.codeblocks.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author andy.an
 * @since 2018/10/15
 */
public class NettyClient {

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new FirstClientHandler());
                    }
                });

        bootstrap.connect("localhost", 9999)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("Connect success");
                    } else {
                        System.out.println("Connect failed");
                    }
                });

    }

    static class FirstClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println(new Date() + ": 客户端写出数据");

            ByteBuf byteBuf = getByteBuf(ctx);

            ctx.channel().writeAndFlush(byteBuf);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;

            System.out.println(new Date() + ": Received data from server --> " + byteBuf.toString(Charset.forName("UTF-8")));

        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
            // 1. 获取二进制抽象 ByteBuf
            ByteBuf byteBuf = ctx.alloc().buffer();

            // 2. 准备数据，指定字符串的字符集为 utf-8
            byte[] bytes = "Hello Netty!".getBytes(Charset.forName("UTF-8"));

            // 3. 填充数据到 ByteBuf
            byteBuf.writeBytes(bytes);

            return byteBuf;
        }
    }
}
