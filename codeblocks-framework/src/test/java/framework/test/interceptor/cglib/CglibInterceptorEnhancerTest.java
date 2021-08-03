package framework.test.interceptor.cglib;

import com.andyadc.codeblocks.framework.interceptor.AnnotatedInterceptor;
import com.andyadc.codeblocks.framework.interceptor.cglib.CglibInterceptorEnhancer;
import framework.test.interceptor.EchoService;
import org.junit.jupiter.api.Test;

/**
 * {@link CglibInterceptorEnhancer} Test
 */
public class CglibInterceptorEnhancerTest {

	@Test
	public void test() {
		CglibInterceptorEnhancer enhancer = new CglibInterceptorEnhancer();
		EchoService echoService = new EchoService();
		Object proxy = enhancer.enhance(echoService, AnnotatedInterceptor.loadInterceptors());
		EchoService echoServiceProxy = (EchoService) proxy;
		echoServiceProxy.echo("Hello,World");
	}
}
