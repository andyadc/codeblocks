package com.andyadc.tinyrpc.codec;

import com.andyadc.tinyrpc.InvocationRequest;
import com.andyadc.tinyrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf byteBuf) throws Exception {
		if (message instanceof InvocationRequest) {
			Serializer serializer = Serializer.DEFAULT;
			byte[] data = serializer.serialize(message);
			byteBuf.writeInt(data.length);
			byteBuf.writeBytes(data);
		}
	}
}
