package com.andyadc.tinyrpc.service.registry;

import com.andyadc.tinyrpc.service.ServiceInstance;
import com.andyadc.tinyrpc.util.ServiceLoaders;

import java.util.List;
import java.util.Map;

public interface ServiceRegistry {

	ServiceRegistry DEFAULT = ServiceLoaders.loadDefault(ServiceRegistry.class);

	void initialize(Map<String, Object> config);

	void register(ServiceInstance serviceInstance);

	void deregister(ServiceInstance serviceInstance);

	List<ServiceInstance> getServiceInstances(String serviceName);

	void close();
}
