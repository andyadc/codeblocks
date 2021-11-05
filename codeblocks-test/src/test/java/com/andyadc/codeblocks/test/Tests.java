package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.framework.http.HttpRequestException;
import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import org.junit.jupiter.api.Test;

/**
 * assert关键字用法简单，但是使用assert往往会让你陷入越来越深的陷阱中。应避免使用。笔者经过研究，总结了以下原因：
 * <p>
 * 1、assert关键字需要在运行时候显式开启才能生效，否则你的断言就没有任何意义。而现在主流的Java IDE工具默认都没有开启-ea断言检查功能。这就意味着你如果使用IDE工具编码，调试运行时候会有一定的麻烦。并且，对于Java Web应用，程序代码都是部署在容器里面，你没法直接去控制程序的运行，如果一定要开启-ea的开关，则需要更改Web容器的运行配置参数。这对程序的移植和部署都带来很大的不便。
 * <p>
 * 2、用assert代替if是陷阱之二。assert的判断和if语句差不多，但两者的作用有着本质的区别：assert关键字本意上是为测试调试程序时使用的，但如果不小心用assert来控制了程序的业务流程，那在测试调试结束后去掉assert关键字就意味着修改了程序的正常的逻辑。
 * <p>
 * 3、assert断言失败将面临程序的退出。这在一个生产环境下的应用是绝不能容忍的。一般都是通过异常处理来解决程序中潜在的错误。但是使用断言就很危险，一旦失败系统就挂了。
 */
public class Tests {

	@Test
	public void testTry() {
		try {
			System.out.println("try");
			throw new HttpRequestException("aaa");
		} catch (Exception e) {
			System.out.println("catch");
			throw new HttpRequestException(e);
		} finally {
			System.out.println("finally");
		}
	}

	@Test
	public void test001() {
		long t1 = System.nanoTime();
		ThreadUtil.sleep(100L);
		long t2 = System.nanoTime();
		System.out.printf("Received response for %s in %.1fms %n%s%n",
			"123", (t2 - t1) / 1e6d, "333");
	}

	@Test
	public void testAssert() {
		String url = null;
		assert url != null : "url is null";
		System.out.println(url);
	}
}
