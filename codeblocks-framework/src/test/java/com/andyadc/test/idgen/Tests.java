package com.andyadc.test.idgen;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * andy.an
 */
public class Tests {
	public static void main(String[] args) throws SQLException {
		SegmentService service = new SegmentService();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CopyOnWriteArraySet<String> ids = new CopyOnWriteArraySet<>();
		Set<String> css = Collections.newSetFromMap(new ConcurrentHashMap<>());

		for (int i = 0; i < 100; i++) {
			executorService.execute(() ->
				{
					String id = service.getId("test").getId();
					ids.add(id);
					css.add(id);
				}
			);
		}
		executorService.shutdown();
		while (true) {
			if (executorService.isTerminated()) {
				System.out.println("Terminated");
				break;
			}
		}
		System.out.println(ids);
		System.out.println(ids.size());
		System.out.println(css);
		System.out.println(css.size());
	}
}
