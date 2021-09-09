package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.ComponentEnhancer;
import com.andyadc.codeblocks.interceptor.DefaultComponentEnhancer;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultComponentEnhancer} Test
 */
public class DefaultComponentEnhancerTest {

	private final ComponentEnhancer interceptorEnhancer = new DefaultComponentEnhancer();

	@Test
	public void testInterface() {
		EchoService echoService = new EchoService();
		echoService = interceptorEnhancer.enhance(echoService);
		echoService.init();
		echoService.echo("Hello,World");
	}
}
