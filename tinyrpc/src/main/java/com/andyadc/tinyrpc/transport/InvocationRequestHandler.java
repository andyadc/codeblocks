package com.andyadc.tinyrpc.transport;

import com.andyadc.tinyrpc.InvocationRequest;
import com.andyadc.tinyrpc.InvocationResponse;
import com.andyadc.tinyrpc.context.ServiceContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvocationRequestHandler extends SimpleChannelInboundHandler<InvocationRequest> {

	private static final Logger logger = LoggerFactory.getLogger(InvocationRequestHandler.class);

	private final ServiceContext serviceContext;

	public InvocationRequestHandler(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, InvocationRequest request) throws Exception {
		String serviceName = request.getServiceName();
		String methodName = request.getMethodName();
		Object[] parameters = request.getParameters();
		Class<?>[] parameterTypes = request.getParameterTypes();

		Object service = serviceContext.getService(serviceName);
		Object entity = null;
		String errorMessage = null;
		try {
			entity = MethodUtils.invokeMethod(service, methodName, parameters, parameterTypes);
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}

		InvocationResponse response = new InvocationResponse();
		response.setRequestId(request.getRequestId());
		response.setEntity(entity);
		response.setErrorMessage(errorMessage);

		ctx.writeAndFlush(response);

		logger.info("Write and Flush {}", response);
	}
}
