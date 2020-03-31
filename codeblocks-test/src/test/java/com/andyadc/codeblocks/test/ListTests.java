package com.andyadc.codeblocks.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * List 的坑
 * andy.an
 * 2020/3/31
 */
public class ListTests {

	private static final Logger logger = LoggerFactory.getLogger(ListTests.class);

	@Test
	public void testAsList() {
		int[] array = {1, 2, 3};
		List list = Arrays.asList(array);
		// list:[[I@6a4f787b] size:1 class:class [I
		logger.info("list:{} size:{} class:{}", list, list.size(), list.get(0).getClass());

		Integer[] array2 = {1, 2, 3};
		List<Integer> list2 = Arrays.asList(array2);
		// list:[1, 2, 3] size:3 class:class java.lang.Integer
		logger.info("list:{} size:{} class:{}", list2, list2.size(), list2.get(0).getClass());

		int[] array3 = {1, 2, 3};
		List list3 = Arrays.stream(array3).boxed().collect(Collectors.toList());
		// list:[1, 2, 3] size:3 class:class java.lang.Integer
		logger.info("list:{} size:{} class:{}", list3, list3.size(), list3.get(0).getClass());


		List<Integer> list1 = IntStream.rangeClosed(1, 1000).boxed().collect(Collectors.toList());
		System.out.println(list1);
	}
}
