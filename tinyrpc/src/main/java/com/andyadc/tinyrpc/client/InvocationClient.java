package com.andyadc.tinyrpc.client;

import com.andyadc.tinyrpc.codec.MessageDecoder;
import com.andyadc.tinyrpc.codec.MessageEncoder;
import com.andyadc.tinyrpc.service.ServiceInstance;
import com.andyadc.tinyrpc.transport.InvocationResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class InvocationClient implements AutoCloseable {

	private static final int PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();

	private final String host;

	private final int port;

	private Bootstrap bootstrap;

	private EventLoopGroup group;

	public InvocationClient(ServiceInstance serviceInstance) {
		this.host = serviceInstance.getHost();
		this.port = serviceInstance.getPort();
	}

	public void start() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(PROCESSOR_NUM);
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast("request-encoder", new MessageEncoder());
					channel.pipeline().addLast("response-decoder", new MessageDecoder());
					channel.pipeline().addLast("response-handler", new InvocationResponseHandler());
				}
			});
	}

	public ChannelFuture connect() {
		return bootstrap.connect(host, port);
	}

	@Override
	public void close() throws Exception {
		group.shutdownGracefully();
	}
}
