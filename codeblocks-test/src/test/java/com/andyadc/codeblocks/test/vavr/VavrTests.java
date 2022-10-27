package com.andyadc.codeblocks.test.vavr;

import io.vavr.collection.List;
import io.vavr.collection.Queue;
import org.junit.jupiter.api.Test;

public class VavrTests {

	@Test
	public void test001() {
		Queue<Integer> queue = Queue.of(1, 2, 5);
		List<Integer> list = List.of(3, 6, 9);

		System.out.println(queue);
		System.out.println(list);
	}
}
