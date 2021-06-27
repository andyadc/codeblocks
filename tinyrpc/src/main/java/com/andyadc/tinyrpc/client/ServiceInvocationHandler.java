package com.andyadc.tinyrpc.client;

import com.andyadc.tinyrpc.InvocationRequest;
import com.andyadc.tinyrpc.loadbalancer.ServiceInstanceSelector;
import com.andyadc.tinyrpc.service.ServiceInstance;
import com.andyadc.tinyrpc.service.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServiceInvocationHandler implements InvocationHandler {

	private final String serviceName;

	private final ServiceRegistry serviceRegistry;

	private final ServiceInstanceSelector selector;

	public ServiceInvocationHandler(String serviceName,
									ServiceRegistry serviceRegistry,
									ServiceInstanceSelector selector) {
		this.serviceName = serviceName;
		this.serviceRegistry = serviceRegistry;
		this.selector = selector;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isObjectDeclaredMethod(method)) {
			return handleObjectMethod(proxy, method, args);
		}

		InvocationRequest request = createRequest(method, args);

		return execute(request, proxy);
	}

	private Object execute(InvocationRequest request, Object proxy) {
		ServiceInstance serviceInstance = selectServiceProviderInstance();

		InvocationClient invocationClient = new InvocationClient(serviceInstance);
		ChannelFuture channelFuture = invocationClient.connect().awaitUninterruptibly();

		sendRequest(request, channelFuture);

		ExchangeFuture exchangeFuture = ExchangeFuture.createExchangeFuture(request);

		try {
			return exchangeFuture.get(1000, TimeUnit.MICROSECONDS);
		} catch (Exception e) {
			ExchangeFuture.removeExchangeFuture(request.getRequestId());
		}

		throw new IllegalStateException("Invocation failed!");
	}

	private void sendRequest(InvocationRequest request, ChannelFuture channelFuture) {
		channelFuture.channel().writeAndFlush(request);
	}

	private ServiceInstance selectServiceProviderInstance() {
		List<ServiceInstance> serviceInstances = serviceRegistry.getServiceInstances(serviceName);
		return selector.select(serviceInstances);
	}

	private InvocationRequest createRequest(Method method, Object[] args) {
		InvocationRequest request = new InvocationRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setServiceName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setParameters(args);
		// TODO
		request.setMetadata(new HashMap<>());
		return request;
	}

	private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
		String methodName = method.getName();
		switch (methodName) {
			case "equals":
				// TODO
				break;
			case "hashCode":
				// TODO
				break;
			case "toString":
				// TODO
				break;
		}
		return null;
	}

	private boolean isObjectDeclaredMethod(Method method) {
		return Object.class == method.getDeclaringClass();
	}
}
