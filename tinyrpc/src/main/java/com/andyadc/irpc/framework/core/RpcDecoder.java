package com.andyadc.irpc.framework.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.andyadc.irpc.framework.core.RpcConstants.MAGIC_NUMBER;

public class RpcDecoder extends ByteToMessageDecoder {

	/**
	 * 协议的开头部分的标准长度
	 */
	public final int BASE_LENGTH = 2 + 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= BASE_LENGTH) {
			// 防止收到一些体积过大的数据包
			if (in.readableBytes() > 1000) {
				in.skipBytes(in.readableBytes());
			}
			int beginReader;
			while (true) {
				beginReader = in.readerIndex();
				in.markReaderIndex();
				//这里对应了 RpcProtocol 的魔数
				if (in.readShort() == MAGIC_NUMBER) {
					break;
				} else {
					// 不是魔数开头，说明是非法的客户端发来的数据包
					ctx.close();
					return;
				}
			}
			// 这里对应了 RpcProtocol 对象的 contentLength 字段
			int length = in.readInt();
			// 说明剩余的数据包不是完整的，这里需要重置下读索引
			if (in.readableBytes() < length) {
				in.readerIndex(beginReader);
				return;
			}
			// 这里其实就是实际的 RpcProtocol 对象的 content 字段
			byte[] data = new byte[length];
			in.readBytes(data);
			RpcProtocol rpcProtocol = new RpcProtocol(data);
			out.add(rpcProtocol);
		}
	}
}
