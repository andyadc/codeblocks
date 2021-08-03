package framework.test.interceptor;

import com.andyadc.codeblocks.framework.interceptor.AnnotatedInterceptor;
import com.andyadc.codeblocks.framework.interceptor.ChainableInvocationContext;
import com.andyadc.codeblocks.framework.interceptor.ReflectiveMethodInvocationContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * {@link ChainableInvocationContext} Test
 */
public class ChainableInvocationContextTest {

	@Test
	public void test() throws Exception {
		EchoService echoService = new EchoService();
		Method method = EchoService.class.getMethod("echo", String.class);
		ReflectiveMethodInvocationContext delegateContext = new ReflectiveMethodInvocationContext
			(echoService, method, "Hello,World");

		ChainableInvocationContext context = new ChainableInvocationContext(delegateContext, AnnotatedInterceptor.loadInterceptors());

		context.proceed();
	}
}
