package com.andyadc.tinyrpc.util;

import java.util.ServiceLoader;

public abstract class ServiceLoaders {

	public static <T> T loadDefault(Class<T> serviceClass) {
		return ServiceLoader.load(serviceClass).iterator().next();
	}
}
