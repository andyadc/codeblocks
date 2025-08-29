package com.andyadc.irpc.framework.core;

import com.andyadc.codeblocks.common.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

import static com.andyadc.irpc.framework.core.CommonServerCache.PROVIDER_CLASS_MAP;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 服务端接收数据的时候统一以 RpcProtocol 协议的格式接收，具体的发送逻辑见文章下方客户端发送部分
		RpcProtocol rpcProtocol = (RpcProtocol) msg;
		String json = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());
		RpcInvocation rpcInvocation = JsonUtils.parse(json, RpcInvocation.class);

		//这里的 PROVIDER_CLASS_MAP 就是一开始预先在启动时候存储的 Bean 集合
		Object aimObject = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
		Method[] methods = aimObject.getClass().getDeclaredMethods();
		Object result = null;
		for (Method method : methods) {
			if (method.getName().equals(rpcInvocation.getTargetMethod())) {
				// 通过反射找到目标对象，然后执行目标方法并返回对应值
				if (method.getReturnType().equals(Void.TYPE)) {
					method.invoke(aimObject, rpcInvocation.getArgs());
				} else {
					result = method.invoke(aimObject, rpcInvocation.getArgs());
				}
				break;
			}
		}
		rpcInvocation.setResponse(result);
		RpcProtocol respRpcProtocol = new RpcProtocol(JsonUtils.toJSONString(rpcInvocation).getBytes());
		ctx.writeAndFlush(respRpcProtocol);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		Channel channel = ctx.channel();
		if (channel.isActive()) {
			ctx.close();
		}
	}
}
