package com.andyadc.codeblocks.test.jvm;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GCTest {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("----------------------------");
		System.out.println(Arrays.asList(args));
		System.out.println("----------------------------");

		IntStream.rangeClosed(1, 10).mapToObj(i -> new Thread(() -> {
			while (true) {
				String payload = IntStream.rangeClosed(1, 10000000)
					.mapToObj(__ -> "a")
					.collect(Collectors.joining("")) + UUID.randomUUID().toString();

				try {
					TimeUnit.SECONDS.sleep(10L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(payload.length());
			}
		})).forEach(Thread::start);

		TimeUnit.HOURS.sleep(1L);
		System.out.println("============================");
	}
}
