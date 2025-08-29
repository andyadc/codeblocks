package com.andyadc.irpc.framework.core;

import com.andyadc.codeblocks.common.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.andyadc.irpc.framework.core.CommonClientCache.RESP_MAP;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcProtocol rpcProtocol = (RpcProtocol) msg;
		byte[] reqContent = rpcProtocol.getContent();
		String json = new String(reqContent);
		RpcInvocation rpcInvocation = JsonUtils.parse(json, RpcInvocation.class);
		if (!RESP_MAP.containsKey(rpcInvocation.getUid())) {
			throw new IllegalArgumentException("server response is error!");
		}
		RESP_MAP.put(rpcInvocation.getUid(), rpcInvocation);
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		Channel channel = ctx.channel();
		if (channel.isActive()) {
			ctx.close();
		}
	}
}
