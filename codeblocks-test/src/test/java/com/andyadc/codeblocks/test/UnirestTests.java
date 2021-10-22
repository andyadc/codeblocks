package com.andyadc.codeblocks.test;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * http://kong.github.io/unirest-java/
 */
public class UnirestTests {

	@Test
	public void test001() {
		HttpResponse<String> response = Unirest.post("http://localhost:9999/hello/")
			.header("x-custom-header", "hello")
			.field("fruit", "apple")
			.field("droid", "R2D2")
			.asString();
		System.out.println(response.getBody());
	}

	@Test
	public void test002() {
		HttpResponse<String> response = Unirest.post("http://localhost:9999/hello/")
			.body("This is the entire body")
			.asString();
		System.out.println(response.getBody());
	}

	@Test
	public void test003() {
		Unirest.post("http://localhost:9999/hello/upload")
			.field("image", new File("D:\\temp\\1.png"))
			.asEmpty();
	}
}
