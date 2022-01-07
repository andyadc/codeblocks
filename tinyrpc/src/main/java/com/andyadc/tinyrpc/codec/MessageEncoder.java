package com.andyadc.tinyrpc.codec;

import com.andyadc.tinyrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageEncoder extends MessageToByteEncoder<Object> {

	private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf byteBuf) throws Exception {
		Serializer serializer = Serializer.DEFAULT;
		byte[] data = serializer.serialize(message);
		byteBuf.writeInt(data.length);
		byteBuf.writeBytes(data);

		logger.info("Encode {} to bytes[length: {}]", message, data.length);
	}
}
