package com.andyadc.codeblocks.test.file;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class TextFileLineByLineReaderTests {

	@Test
	public void test01() throws Exception {
		String path = "D:\\temp\\service-id-b";
		BufferedReader reader = new BufferedReader(new FileReader(path));

		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		reader.close();
	}

	@Test
	public void test02() throws Exception {
		String path = "D:\\temp\\service-id-b";
		FileReader reader = new FileReader(path);

		int i;
		while ((i = reader.read()) != -1) {
			System.out.print((char) i);
		}
	}

	@Test
	public void test03() throws Exception {
		String path = "D:\\temp\\service-id-b";
		File file = new File(path);

		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			System.out.println(sc.nextLine());
		}
	}

	@Test
	public void test04() throws Exception {
		String fileName = "D:\\temp\\service-id-b";

		List<String> lines = Files.readAllLines(Paths.get(fileName));
		System.out.println(lines);
	}

	@Test
	public void test05() throws Exception {
		String fileName = "D:\\temp\\service-id-b";

		Stream<String> stream = Files.lines(Paths.get(fileName));
		stream.forEach(System.out::println);
	}

	@Test
	public void test06() throws Exception {
		String fileName = "D:\\temp\\service-id-b";
		File file = new File(fileName);

		BufferedReader br = new BufferedReader(new FileReader(file));
		br.lines().forEach(System.out::println);
	}

	@Test
	public void test07() throws Exception {
		String fileName = "D:\\temp\\service-id-b";

		List<String> lines = IOUtils.readLines(new FileReader(fileName));
		System.out.println(lines);
	}
}
