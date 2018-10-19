package com.andyadc.codeblocks.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author andy.an
 * @since 2018/10/15
 */
public class NettyServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
//                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new FirstServerHandler());

                        // inBound，处理读数据的逻辑链
//                        ch.pipeline().addLast(new InBoundHandlerA());
//                        ch.pipeline().addLast(new InBoundHandlerB());
//                        ch.pipeline().addLast(new InBoundHandlerC());

                        // outBound，处理写数据的逻辑链
//                        ch.pipeline().addLast(new OutBoundHandlerA());
//                        ch.pipeline().addLast(new OutBoundHandlerB());
//                        ch.pipeline().addLast(new OutBoundHandlerC());
                    }
                });

        bootstrap.bind(9999);
    }

    static class OutBoundHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerA: " + msg);
            super.write(ctx, msg, promise);
        }
    }

    static class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerB: " + msg);
            super.write(ctx, msg, promise);
        }
    }

    static class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutBoundHandlerC: " + msg);
            super.write(ctx, msg, promise);
        }
    }

    static class InBoundHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerA: " + msg);
            super.channelRead(ctx, msg);
        }
    }

    static class InBoundHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerB: " + msg);
            super.channelRead(ctx, msg);
        }
    }

    static class InBoundHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InBoundHandlerC: " + msg);
            super.channelRead(ctx, msg);
        }
    }

    static class FirstServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;

            System.out.println(new Date() + ": Received data from client --> " + byteBuf.toString(Charset.forName("UTF-8")));

            ByteBuf out = getByteBuf(ctx);
            ctx.channel().writeAndFlush(out);
        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
            // 1. 获取二进制抽象 ByteBuf
            ByteBuf byteBuf = ctx.alloc().buffer();

            // 2. 准备数据，指定字符串的字符集为 utf-8
            byte[] bytes = "Hello Postman !".getBytes(Charset.forName("UTF-8"));

            // 3. 填充数据到 ByteBuf
            byteBuf.writeBytes(bytes);

            return byteBuf;
        }
    }
}
