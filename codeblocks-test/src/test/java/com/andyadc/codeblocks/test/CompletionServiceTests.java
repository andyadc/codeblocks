package com.andyadc.codeblocks.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompletionServiceTests {

	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
		Callable<Integer> callable = () -> {
			TimeUnit.SECONDS.sleep(1L);
			return 1;
		};

		for (int i = 0; i < 5; i++) {
			completionService.submit(callable);
		}

		for (int i = 0; i < 5; i++) {
			Future<Integer> future = completionService.take();
			System.out.println(future.get());
		}

		executor.shutdown();
	}


}
