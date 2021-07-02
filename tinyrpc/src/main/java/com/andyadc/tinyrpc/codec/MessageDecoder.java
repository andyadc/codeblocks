package com.andyadc.tinyrpc.codec;

import com.andyadc.tinyrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
		byteBuf.markReaderIndex();
		int dataLen = byteBuf.readInt();
		if (byteBuf.readableBytes() < dataLen) {
			byteBuf.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLen];
		byteBuf.readBytes(data);
		Serializer serializer = Serializer.DEFAULT;
		Object result = serializer.deserialize(data, Object.class);
		list.add(result);
	}
}
