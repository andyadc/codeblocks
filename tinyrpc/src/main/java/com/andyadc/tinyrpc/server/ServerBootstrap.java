package com.andyadc.tinyrpc.server;

import com.andyadc.tinyrpc.codec.InvocationRequestEncoder;
import com.andyadc.tinyrpc.codec.InvocationResponseDecoder;
import com.andyadc.tinyrpc.context.ServiceContext;
import com.andyadc.tinyrpc.service.DefaultServiceInstance;
import com.andyadc.tinyrpc.service.registry.ServiceRegistry;
import com.andyadc.tinyrpc.transport.InvocationRequestHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Collections;

public class ServerBootstrap implements AutoCloseable {

	private final String applicationName;

	private final int port;

	private final ServiceContext serviceContext;

	private final ServiceRegistry serviceRegistry;

	private Bootstrap bootstrap;

	private EventLoopGroup group;

	public ServerBootstrap(String applicationName, int port) {
		this.applicationName = applicationName;
		this.port = port;
		this.serviceContext = ServiceContext.DEFAULT;
		this.serviceRegistry = ServiceRegistry.DEFAULT;
	}

	public ServerBootstrap registerService(String serviceName, Object service) {
		serviceContext.registerService(serviceName, service);
		return this;
	}

	public ServerBootstrap start() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("request-encoder", new InvocationRequestEncoder());
					ch.pipeline().addLast("response-decoder", new InvocationResponseDecoder());
					ch.pipeline().addLast("request-handler", new InvocationRequestHandler(serviceContext));
				}
			});

		ChannelFuture channelFuture = bootstrap.bind(port);
		registerServer();

		try {
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private void registerServer() {
		DefaultServiceInstance serviceInstance = new DefaultServiceInstance();
		serviceInstance.setHost("127.0.0.1");
		serviceInstance.setPort(port);
		serviceInstance.setServiceName(applicationName);
		serviceRegistry.initialize(Collections.emptyMap());
		serviceRegistry.register(serviceInstance);
	}

	@Override
	public void close() throws Exception {
		group.shutdownGracefully();
	}
}
