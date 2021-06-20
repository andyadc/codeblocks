package com.andyadc.tinyrpc.service.registry.zookeeper;

import com.andyadc.tinyrpc.service.ServiceInstance;
import com.andyadc.tinyrpc.service.registry.ServiceRegistry;

import java.util.List;
import java.util.Map;

public class ZookeeperServiceRegistry implements ServiceRegistry {

	@Override
	public void initialize(Map<String, Object> config) {

	}

	@Override
	public void register(ServiceInstance serviceInstance) {

	}

	@Override
	public void deregister(ServiceInstance serviceInstance) {

	}

	@Override
	public List<ServiceInstance> getServiceInstances(String serviceName) {
		return null;
	}

	@Override
	public void close() {

	}
}
