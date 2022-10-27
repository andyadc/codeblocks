package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class LongAdder_LongAccumulator_Tests {

	@Test
	public void testLongAdder() {
		LongAdder counter = new LongAdder();
		ExecutorService executorService = Executors.newFixedThreadPool(8);

		int numberOfThreads = 4;
		int numberOfIncrements = 100;

		Runnable incrementAction = () -> IntStream
			.range(0, numberOfIncrements)
			.forEach(i -> counter.increment());

		for (int i = 0; i < numberOfThreads; i++) {
			executorService.execute(incrementAction);
		}

		System.out.println(counter.sum());
	}
}
