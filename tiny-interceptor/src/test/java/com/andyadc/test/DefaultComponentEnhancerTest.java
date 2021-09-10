package com.andyadc.test;

import com.andyadc.codeblocks.common.util.CollectionUtils;
import com.andyadc.codeblocks.interceptor.ComponentEnhancer;
import com.andyadc.codeblocks.interceptor.DefaultComponentEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link DefaultComponentEnhancer} Test
 */
public class DefaultComponentEnhancerTest {

	private final ComponentEnhancer interceptorEnhancer = new DefaultComponentEnhancer();

	@Test
	public void testInterface() {
		EchoService echoService = new EchoService();
		ExternalInterceptor interceptor = new ExternalInterceptor();
		echoService = interceptorEnhancer.enhance(echoService, interceptor);
		echoService.init();
		echoService.echo("Hello,World");

		assertEquals(CollectionUtils.asSet("init", "echo"), interceptor.getMethodNames());
	}
}
