package com.andyadc.codeblocks.test.file;

import com.andyadc.codeblocks.kit.collection.CollectionUtil;
import com.andyadc.codeblocks.kit.text.StringUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class TextFileLineByLineReaderTests {

	@Test
	public void testMemberCodesExtract() throws Exception {
		String path = "D:\\temp\\membercodes_03221.txt";
		BufferedReader reader = new BufferedReader(new FileReader(path));

		List<String> total = new ArrayList<>();
		Set<String> totalSet = new HashSet<>();
		String line;
		while ((line = reader.readLine()) != null) {
			total.add(line);
			totalSet.add(line);
		}
		System.out.println(total.size());
		System.out.println(totalSet.size());

		path = "D:\\temp\\membercodes_error.txt";
		reader = new BufferedReader(new FileReader(path));

		List<String> error = new ArrayList<>();
		List<String> errorSet = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			error.add(line);
			errorSet.add(line);
		}
		System.out.println(error.size());
		System.out.println(errorSet.size());

		Collection<?> collection = CollectionUtil.sub(total, error);
		System.out.println(collection.size());

		List<String> result = new ArrayList<>();
		int count = 0;
		for (String s : total) {
			if (error.contains(s)) {
				count++;
			}
			if (!error.contains(s)) {
				result.add(s);
			}
		}
		System.out.println(count);
		System.out.println(result.size());
//		System.out.println(result);

		for (String s : result) {
			System.out.println(s);
		}
	}

	@Test
	public void testServiceIdDiff() throws Exception {
		String path = "D:\\temp\\service_id\\service-id";
		BufferedReader reader = new BufferedReader(new FileReader(path));

		List<String> idList = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			idList.add(line);
		}
		System.out.println(idList.size());

		path = "D:\\temp\\service_id\\service-id-prd";
		reader = new BufferedReader(new FileReader(path));

		List<String> prdIdList = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			prdIdList.add(line);
		}
		System.out.println(prdIdList.size());

		Collection<?> collection = CollectionUtil.intersect(idList, prdIdList);
		System.out.println(collection.size());
//		System.out.println(collection);

		Collection<?> diff = CollectionUtil.sub(idList, prdIdList);
		System.out.println(diff.size());
//		System.out.println(diff);
		diff.forEach(System.out::println);

		StringBuilder builder = new StringBuilder("(");
		for (Object o : diff) {
			String id = (String) o;
			builder.append("'").append(id).append("', ");
		}
		builder.append(")");
//		System.out.println(builder.toString());

		reader.close();
	}

	@Test
	public void testServiceId() throws Exception {
		String path = "D:\\temp\\service_id\\service-id-prd-2022";
		BufferedReader reader = new BufferedReader(new FileReader(path));

		List<String> idList = new ArrayList<>();
		Set<String> idSet = new HashSet<>();
		String line;
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			idList.add(line);
			if (idSet.contains(line)) {
//				System.out.println(line);
				continue;
			}
			idSet.add(line);
		}

		System.out.println(idList.size());
		System.out.println(idSet.size());

		idSet.forEach(System.out::println);

		reader.close();
	}

	@Test
	public void test01() throws Exception {
		String path = "D:\\temp\\service-id-c";
		BufferedReader reader = new BufferedReader(new FileReader(path));

		String line;
		while ((line = reader.readLine()) != null) {
			if (StringUtil.isNotBlank(line) && line.contains(".method")) {
				int dotIndex = line.indexOf(".");
				System.out.println(line.substring(0, dotIndex));
			}
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
