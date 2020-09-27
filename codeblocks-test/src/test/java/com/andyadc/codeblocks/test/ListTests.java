package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
	public void testAdd() {
		List<String> list1 = new ArrayList<>();
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<>();
		list2.add("a");
		list2.add("b");
		list2.add("c");

		System.out.println(list1.size());
		System.out.println(list2.size());

		list1.addAll(list2);
		System.out.println(list1.size());

		List<String> list3 = new ArrayList<>();
		list3.add("\uD83C\uDF49");
		list3.add("\uD83D\uDD25");
		list3.add("\uD83D\uDE02");

		List<String> list4 = new ArrayList<>();
		list4.addAll(list2);
		list4.addAll(list3);

		System.out.println(list4);
	}

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
