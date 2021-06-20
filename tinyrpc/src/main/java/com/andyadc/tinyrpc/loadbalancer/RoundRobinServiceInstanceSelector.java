package com.andyadc.tinyrpc.loadbalancer;

import com.andyadc.tinyrpc.service.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinServiceInstanceSelector implements ServiceInstanceSelector {

	private final AtomicInteger counter = new AtomicInteger();

	@Override
	public ServiceInstance select(List<ServiceInstance> serviceInstances) {
		int size = serviceInstances.size();
		int count = counter.getAndIncrement();
		int index = (count - 1) % size;
		return serviceInstances.get(index);
	}
}
