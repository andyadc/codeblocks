package com.andyadc.test.cglib;

import com.andyadc.codeblocks.interceptor.Interceptor;
import com.andyadc.codeblocks.interceptor.cglib.CglibInterceptorEnhancer;
import com.andyadc.test.EchoService;
import org.junit.jupiter.api.Test;

/**
 * {@link CglibInterceptorEnhancer} Test
 */
public class CglibInterceptorEnhancerTest {

	@Test
	public void test() {
		CglibInterceptorEnhancer enhancer = new CglibInterceptorEnhancer();
		EchoService echoService = new EchoService();
		Object proxy = enhancer.enhance(echoService, Interceptor.loadInterceptors());
		EchoService echoServiceProxy = (EchoService) proxy;
		echoServiceProxy.echo("Hello,World");
	}
}
