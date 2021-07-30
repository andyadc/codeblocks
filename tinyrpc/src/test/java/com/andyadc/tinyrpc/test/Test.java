package com.andyadc.tinyrpc.test;

import com.andyadc.tinyrpc.loadbalancer.ServiceInstanceSelector;
import com.andyadc.tinyrpc.util.ServiceLoaders;

public class Test {

	public static void main(String[] args) {
		ServiceInstanceSelector selector = ServiceLoaders.loadDefault(ServiceInstanceSelector.class);
		System.out.println(selector);
	}
}
