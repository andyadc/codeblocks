package com.andyadc.test.cglib;

import com.andyadc.codeblocks.interceptor.Interceptor;
import com.andyadc.codeblocks.interceptor.cglib.CglibComponentEnhancer;
import com.andyadc.test.EchoService;
import org.junit.jupiter.api.Test;

/**
 * {@link CglibComponentEnhancer} Test
 */
public class CglibComponentEnhancerTest {

	@Test
	public void test() {
		CglibComponentEnhancer enhancer = new CglibComponentEnhancer();
		EchoService echoService = new EchoService();
		Object proxy = enhancer.enhance(echoService, Interceptor.loadInterceptors());
		EchoService echoServiceProxy = (EchoService) proxy;
		echoServiceProxy.echo("Hello,World");
	}
}
