package com.andyadc.tinyrpc.context;

import static com.andyadc.tinyrpc.util.ServiceLoaders.loadDefault;

public interface ServiceContext {

	ServiceContext DEFAULT = loadDefault(ServiceContext.class);

	boolean registerService(String serviceName, Object service);

	Object getService(String serviceName);
}
