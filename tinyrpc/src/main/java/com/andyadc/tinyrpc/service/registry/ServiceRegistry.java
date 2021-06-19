package com.andyadc.tinyrpc.service.registry;

import com.andyadc.tinyrpc.service.ServiceInstance;

import java.util.List;
import java.util.Map;

import static com.andyadc.tinyrpc.util.ServiceLoaders.loadDefault;

public interface ServiceRegistry {

	ServiceRegistry DEFAULT = loadDefault(ServiceRegistry.class);

	void initialize(Map<String, Object> config);

	void register(ServiceInstance serviceInstance);

	void deregister(ServiceInstance serviceInstance);

	List<ServiceInstance> getServiceInstances(String serviceName);

	void close();
}
