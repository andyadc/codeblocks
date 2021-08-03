package framework.test.interceptor;

import com.andyadc.codeblocks.framework.interceptor.AnnotatedInterceptor;
import com.andyadc.codeblocks.framework.interceptor.DefaultInterceptorEnhancer;
import com.andyadc.codeblocks.framework.interceptor.InterceptorEnhancer;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultInterceptorEnhancer} Test
 */
public class DefaultInterceptorEnhancerTest {

	private final InterceptorEnhancer interceptorEnhancer = new DefaultInterceptorEnhancer();

	@Test
	public void testInterface() {
		EchoService echoService = new EchoService();
		echoService = interceptorEnhancer.enhance(echoService, AnnotatedInterceptor.loadInterceptors());
		echoService.echo("Hello,World");
	}
}
