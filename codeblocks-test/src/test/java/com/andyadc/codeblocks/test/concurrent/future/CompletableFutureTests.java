package com.andyadc.codeblocks.test.concurrent.future;

import java.util.concurrent.CompletableFuture;

/**
 * andy.an
 * 2020/4/8
 */
public class CompletableFutureTests {

	public static void main(String[] args) throws Exception {
		CompletableFuture<String> queryCodeFromSina = CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://finance.sina.com.cn/code/"));
		CompletableFuture<String> queryCodeFromNetease = CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://money.163.com/code/"));

		CompletableFuture<Object> codeQuery = CompletableFuture.anyOf(queryCodeFromSina, queryCodeFromNetease);

		CompletableFuture<String> fetchPriceFromSina = codeQuery.thenApplyAsync((code) -> fetchPrice((String) code, "https://finance.sina.com.cn/price/"));
		CompletableFuture<String> fetchPriceFromNetease = codeQuery.thenApplyAsync((code) -> fetchPrice((String) code, "https://money.163.com/price/"));

		CompletableFuture<Object> priceQuery = CompletableFuture.anyOf(fetchPriceFromSina, fetchPriceFromNetease);

		priceQuery.thenAccept((result) -> System.out.println("price: \uD83D\uDD28" + result));
		Thread.sleep(2000L);
	}

	static String queryCode(String name, String url) {
		System.out.println("Query code from " + url + " ...");
		long random = (long) (Math.random() * 1000);
		try {
			Thread.sleep(random);
		} catch (InterruptedException e) {
			// ignore
		}
		return random + "";
	}

	static String fetchPrice(String code, String url) {
		System.out.println("Query price from " + url + "...");
		long random = (long) (Math.random() * 1000);
		try {
			Thread.sleep(random);
		} catch (InterruptedException e) {
			// ignore
		}
		return random + "";
	}

}
