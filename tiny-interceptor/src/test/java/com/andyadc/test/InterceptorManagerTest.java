package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.InterceptorManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link InterceptorManager}
 */
public class InterceptorManagerTest {

	@Test
	public void test() {
		InterceptorManager registry = InterceptorManager.getInstance();
		for (int i = 0; i < 99; i++) {
			assertEquals(registry, InterceptorManager.getInstance());
		}
	}
}
