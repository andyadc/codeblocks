package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.DefaultInterceptorEnhancer;
import com.andyadc.codeblocks.interceptor.InterceptorEnhancer;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultInterceptorEnhancer} Test
 */
@Deprecated
public class DefaultInterceptorEnhancerTest {

	private final InterceptorEnhancer interceptorEnhancer = new DefaultInterceptorEnhancer();

	@Test
	public void testInterface() {
		EchoService echoService = new EchoService();
		echoService = interceptorEnhancer.enhance(echoService);
		echoService.init();
		echoService.echo("Hello,World");
	}
}
