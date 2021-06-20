package com.andyadc.tinyrpc.loadbalancer;

import com.andyadc.tinyrpc.service.ServiceInstance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomServiceInstanceSelector implements ServiceInstanceSelector {

	@Override
	public ServiceInstance select(List<ServiceInstance> serviceInstances) {
		int size = serviceInstances.size();
		int index = ThreadLocalRandom.current().nextInt(size);
		return serviceInstances.get(index);
	}
}
