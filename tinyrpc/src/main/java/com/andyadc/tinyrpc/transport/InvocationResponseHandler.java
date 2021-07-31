package com.andyadc.tinyrpc.transport;

import com.andyadc.tinyrpc.InvocationResponse;
import com.andyadc.tinyrpc.client.ExchangeFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvocationResponseHandler extends SimpleChannelInboundHandler<InvocationResponse> {

	private static final Logger logger = LoggerFactory.getLogger(InvocationResponseHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, InvocationResponse response) throws Exception {
		String requestId = response.getRequestId();
		ExchangeFuture exchangeFuture = ExchangeFuture.removeExchangeFuture(requestId);
		logger.info("requestId: {}, {}", requestId, exchangeFuture);
		if (exchangeFuture != null) {
			Object entity = response.getEntity();
			exchangeFuture.getPromise().setSuccess(entity);
		}
	}
}
