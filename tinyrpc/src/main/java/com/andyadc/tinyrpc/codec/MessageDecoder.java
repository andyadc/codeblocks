package com.andyadc.tinyrpc.codec;

import com.andyadc.tinyrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
		byteBuf.markReaderIndex();
		int dataLength = byteBuf.readInt();
		if (byteBuf.readableBytes() < dataLength) {
			byteBuf.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		byteBuf.readBytes(data);
		Serializer serializer = Serializer.DEFAULT;
		Object result = serializer.deserialize(data, Object.class);
		list.add(result);

		logger.info("Serialize from bytes[length: {}] to be a {}", dataLength, result);
	}
}
