package com.andyadc.tinyrpc.loadbalancer;

import com.andyadc.tinyrpc.service.ServiceInstance;

import java.util.List;

import static com.andyadc.tinyrpc.util.ServiceLoaders.loadDefault;

public interface ServiceInstanceSelector {

	ServiceInstanceSelector DEFAULT = loadDefault(ServiceInstanceSelector.class);

	ServiceInstance select(List<ServiceInstance> serviceInstances);
}
