package com.andyadc.tinyrpc.transport;

import com.andyadc.tinyrpc.InvocationResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InvocationResponseHandler extends SimpleChannelInboundHandler<InvocationResponse> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, InvocationResponse response) throws Exception {
		String requestId = response.getRequestId();
		
	}
}
