package com.andyadc.test;

import com.andyadc.codeblocks.interceptor.InterceptorBindingInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link InterceptorBindingInfo} Test
 */
@Logging(name = "InterceptorBindingInfoTest")
public class InterceptorBindingInfoTest {

	@Test
	@Logging(name = "test")
	public void test() throws Throwable {
		Logging logging = this.getClass().getAnnotation(Logging.class);
		InterceptorBindingInfo info = InterceptorBindingInfo.valueOf(logging);
		assertEquals(Logging.class, info.getDeclaredAnnotationType());
		assertFalse(info.isSynthetic());
		assertTrue(info.getAttributes().isEmpty());

		InterceptorBindingInfo info2 = new InterceptorBindingInfo(getClass().getMethod("test").getAnnotation(Logging.class));
		assertEquals(Logging.class, info.getDeclaredAnnotationType());
		assertEquals(Logging.class, info2.getDeclaredAnnotationType());
		assertFalse(info2.isSynthetic());
		assertTrue(info2.getAttributes().isEmpty());

		assertEquals(info, info2);
	}
}
