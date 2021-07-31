package com.andyadc.tinyrpc.service;

import java.io.Serializable;
import java.util.Map;

/**
 * 服务实例
 */
public interface ServiceInstance extends Serializable {

	String getId();

	String getServiceName();

	String getHost();

	int getPort();

	Map<String, String> getMetadata();
}
