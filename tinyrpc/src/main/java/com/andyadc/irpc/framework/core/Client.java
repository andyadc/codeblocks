package com.andyadc.irpc.framework.core;

import com.alibaba.fastjson.JSON;
import com.andyadc.irpc.framework.core.proxy.JDKProxyFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	private ClientConfig clientConfig;

	public RpcReference startClientApplication() throws InterruptedException {
		EventLoopGroup clientGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(clientGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new RpcEncoder());
				ch.pipeline().addLast(new RpcDecoder());
				ch.pipeline().addLast(new ClientHandler());
			}
		});

		ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
		logger.info("============ 服务启动 ============");
		this.startClient(channelFuture);
		return new RpcReference(new JDKProxyFactory());
	}

	private void startClient(ChannelFuture channelFuture) {
		Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
		asyncSendJob.start();
	}

	static class AsyncSendJob implements Runnable {

		private final ChannelFuture channelFuture;

		public AsyncSendJob(ChannelFuture channelFuture) {
			this.channelFuture = channelFuture;
		}

		@Override
		public void run() {
			while (true) {
				try {
					//阻塞模式
					RpcInvocation data = CommonClientCache.SEND_QUEUE.take();
					String json = JSON.toJSONString(data);
					RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
					channelFuture.channel().writeAndFlush(rpcProtocol);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ClientConfig getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}
}
