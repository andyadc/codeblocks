package com.andyadc.codeblocks.test.httpserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class CustomHttpServerHandler extends SimpleChannelInboundHandler<Object> {

	StringBuilder responseData = new StringBuilder();
	private HttpRequest request;

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest request = this.request = (HttpRequest) msg;

			if (HttpUtil.is100ContinueExpected(request)) {
				writeResponse(ctx);
			}

			responseData.setLength(0);
			responseData.append(RequestUtils.formatParams(request));
		}

		responseData.append(RequestUtils.evaluateDecoderResult(request));

		if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;

			responseData.append(RequestUtils.formatBody(httpContent));
			responseData.append(RequestUtils.evaluateDecoderResult(request));

			if (msg instanceof LastHttpContent) {
				LastHttpContent trailer = (LastHttpContent) msg;
				responseData.append(RequestUtils.prepareLastResponse(request, trailer));
				writeResponse(ctx, trailer, responseData);
			}
		}
	}

	private void writeResponse(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
		ctx.write(response);
	}

	private void writeResponse(ChannelHandlerContext ctx, LastHttpContent trailer, StringBuilder responseData) {
		boolean keepAlive = HttpUtil.isKeepAlive(request);

		FullHttpResponse httpResponse = new DefaultFullHttpResponse(
			HttpVersion.HTTP_1_1,
			trailer.decoderResult().isSuccess() ? HttpResponseStatus.OK : HttpResponseStatus.BAD_REQUEST,
			Unpooled.copiedBuffer(responseData.toString(), CharsetUtil.UTF_8)
		);

		httpResponse.headers()
			.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

		if (keepAlive) {
			httpResponse.headers()
				.setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content()
					.readableBytes());
			httpResponse.headers()
				.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}

		ctx.write(httpResponse);

		if (!keepAlive) {
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
				.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
