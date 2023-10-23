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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouterProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(RouterProcessor.class);

	private LinkedHashMap<Map<String, String>, String> routerRules = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {
		RouterRequest request = new RouterRequest();
		request.setCode("key");
		BeanInfo beanInfo = Introspector.getBeanInfo(request.getClass());
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			System.out.println(descriptor.getName());
		}

		Class<? extends RouterRequest> clazz = request.getClass();
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		Field field = clazz.getField("code");
		Object o = field.get(clazz.newInstance());
		System.out.println(o);
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

				try {
					Class<? extends RouterRequest> clazz = request.getClass();
					Field field = clazz.getField(key);
					Object o = field.get(clazz.newInstance());
					Method method = clazz.getMethod(key, String.class);
				} catch (Exception e) {

				}


			}
		}
	}

	public void setRouterRules(LinkedHashMap<Map<String, String>, String> routerRules) {
		this.routerRules = routerRules;
	}
}
