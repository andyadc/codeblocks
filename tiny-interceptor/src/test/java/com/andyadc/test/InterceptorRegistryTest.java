package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.InterceptorRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link InterceptorRegistry}
 */
public class InterceptorRegistryTest {

	@Test
	public void test() {
		InterceptorRegistry registry = InterceptorRegistry.getInstance();
		for (int i = 0; i < 99; i++) {
			assertEquals(registry, InterceptorRegistry.getInstance());
		}
	}
}
