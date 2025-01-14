package com.andyadc.summer.aop.after;

import com.andyadc.summer.context.AnnotationConfigApplicationContext;
import com.andyadc.summer.io.PropertyResolver;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfterProxyTest {

	@Test
	public void testAfterProxy() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AfterApplication.class, createPropertyResolver())) {
			GreetingBean proxy = ctx.getBean(GreetingBean.class);
			// should change return value:
			assertEquals("Hello, Andy!", proxy.hello("Andy"));
			assertEquals("Morning, Alice!", proxy.morning("Alice"));
		}
	}

	PropertyResolver createPropertyResolver() {
		Properties ps = new Properties();
		return new PropertyResolver(ps);
	}
}
