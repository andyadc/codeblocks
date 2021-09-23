package com.andyadc.codeblocks.test.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * https://developer.ibm.com/zh/articles/j-cf-of-jdk8/
 */
public class CompletableFutureTests {

	private static final ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
		final AtomicLong count = new AtomicLong(0);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "custom-executor-" + count.incrementAndGet());
		}
	});

	/**
	 * 创建完整的 CompletableFuture
	 */
	@Test
	public void completedFutureExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
		System.out.println(cf.isDone());
		System.out.println(cf.getNow(null));
	}

	/**
	 * 运行简单的异步场景
	 */
	@Test
	public void runAsyncExample() {
		CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
			System.out.println("isDaemon: " + Thread.currentThread().isDaemon());
			randomSleep();
		});
		System.out.println(cf.isDone());
		sleepEnough();
		System.out.println(cf.isDone());
	}

	/**
	 * 同步执行动作
	 */
	@Test
	public void thenApplyExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> {
			System.out.println("isDaemon: " + Thread.currentThread().isDaemon());
			return s.toUpperCase();
		});
		System.out.println(cf.getNow(null));
	}

	/**
	 * 异步执行动作
	 */
	@Test
	public void thenApplyAsyncExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
			System.out.println("isDaemon: " + Thread.currentThread().isDaemon());
			randomSleep();
			return s.toUpperCase();
		});
		System.out.println(cf.getNow(null));
		System.out.println(cf.join());
	}

	/**
	 * 使用固定的线程池完成异步执行动作
	 */
	@Test
	public void thenApplyAsyncWithExecutorExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
			System.out.println(Thread.currentThread().getName());
			System.out.println(Thread.currentThread().isDaemon());
			randomSleep();
			return s.toUpperCase();
		}, executor);

		System.out.println(cf.getNow(null));
		System.out.println(cf.join());
	}

	/**
	 * 作为消费者消费计算结果
	 */
	@Test
	public void thenAcceptExample() {
		StringBuilder result = new StringBuilder();
		CompletableFuture.completedFuture("thenAccept message").thenAccept(result::append);

		System.out.println(result.length());
	}

	/**
	 * 异步消费
	 */
	@Test
	public void thenAcceptAsyncExample() {
		StringBuilder result = new StringBuilder();
		CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAccept message").thenAcceptAsync(result::append);
		cf.join();

		System.out.println(result.length());
	}

	@Test
	public void completeExceptionallyExample() {
		CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase);
		sleep(1L);

		CompletableFuture<String> exceptionHandler = cf.handle((s, e) -> {
			System.out.println(s);
			return e != null ? "message upper cancel" : "";
		});

		cf.completeExceptionally(new RuntimeException("completed exceptionally"));

		System.out.println(cf.isCompletedExceptionally());

		try {
			cf.join();
			fail("Should have thrown an exception");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println(exceptionHandler.join());
	}

	// ------------------------------------------------------------------

	public void fail(String message) {
		throw new RuntimeException(message);
	}

	private void sleep(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void sleepEnough() {
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void randomSleep() {
		int i = ThreadLocalRandom.current().nextInt(1, 10);
		try {
			TimeUnit.SECONDS.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
