package com.andyadc.workflow.processor.common;

import com.andyadc.workflow.base.RouterRequest;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.BizProcessor;
import com.andyadc.workflow.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouterProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(RouterProcessor.class);

	private LinkedHashMap<Map<String, String>, String> routerRules = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {
		RouterRequest request = new RouterRequest();
		BeanInfo beanInfo = Introspector.getBeanInfo(request.getClass());
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			System.out.println(descriptor.getName());
		}

	}

	@Override
	public void process(ProcessorContext context) throws BizException {

	}

	private void calcRouterRules(RouterRequest request) {
		int size = request.conditionSize();
		for (Map.Entry<Map<String, String>, String> rule : routerRules.entrySet()) {

			for (Map.Entry<String, String> entry : rule.getKey().entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				if (request.getCode().equalsIgnoreCase(value)) {

				}
			}
		}
	}

	public void setRouterRules(LinkedHashMap<Map<String, String>, String> routerRules) {
		this.routerRules = routerRules;
	}
}
