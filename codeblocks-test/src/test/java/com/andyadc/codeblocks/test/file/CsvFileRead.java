package com.andyadc.codeblocks.test.file;

import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvFileRead {

	public static void main(String[] args) throws Exception {
		System.out.println(StandardCharsets.UTF_8.displayName());
		List<String> snList = extractSNList();
		snList.forEach(System.out::println);
		System.out.println(snList.size());
	}

	private static List<String> extractSNList() throws Exception {
		File file = new File("d:\\temp\\cert\\cert_gca.csv");
		Reader reader = new FileReader(file);
		CSVReader csvReader = new CSVReader(reader);
		csvReader.skip(1);

		List<String> snList = new ArrayList<>();
		for (String[] strings : csvReader) {
			snList.add(strings[1]);
		}
		return snList;
	}

	/**
	 * cert: cfca vs kq-99bill
	 */
	@Test
	public void test04() throws Exception {
		InputStream fileInputStream = new FileInputStream("d:\\temp\\cert\\cert_cfca.csv");
		Reader reader = new InputStreamReader(fileInputStream, "GB18030");

		CSVReader csvReader = new CSVReader(reader);
		csvReader.skip(1);

		List<String> gcaSNList = extractSNList();

		Iterator<String[]> iterator = csvReader.iterator();
		int i = 0;
		int j = 0;
		int diff = 0;
		while (iterator.hasNext()) {
			i++;

			String[] strings = iterator.next();
			String sn = strings[0];
			String dn = strings[1];
			sn = sn.trim();
			dn = dn.trim();
			if (gcaSNList.contains(sn)) {
				j++;
//				System.out.printf("Item sn: %s, dn: %s, exp: %s, %n", sn, strings[1], strings[4]);
			}

			String createTime = strings[2].trim();
			String expTime = strings[4].trim();
			if (!gcaSNList.contains(sn)) {
				// CN第三段以@ZK开始是架构组
				if (!dn.contains("@ZK")) {
					diff++;
					System.out.printf("%s,%s,%s,%s%n", sn, dn, createTime, expTime);
				}
			}
		}

		System.out.println("Total: " + i);
		System.out.println("Diff: " + diff);
//		System.out.println("Contains: " + j);
	}

	/**
	 * cert: cfca vs kq
	 */
	@Test
	public void test03() throws Exception {
		File file = new File("d:\\temp\\cert\\cert_gca.csv");
		Reader reader = new FileReader(file);
		CSVReader csvReader = new CSVReader(reader);
		csvReader.skip(1);

		Iterator<String[]> iterator = csvReader.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;

			String[] strings = iterator.next();
			System.out.printf("Item memberCode: %s, sn: %s, exp: %s, dn: %s %n",
				strings[0], strings[1], strings[2], strings[6]);
		}

		System.out.println("Total: " + i);
	}

	/**
	 * OpenCSV
	 */
	@Test
	public void test02() throws Exception {
		File file = new File("d:\\temp\\BPM00218221.csv");
		Reader reader = new FileReader(file);
		CSVReader csvReader = new CSVReader(reader);
		csvReader.skip(10000); // header: [ROWNUM, MEMBERCODE, VALIDDURATION, BEGINDATE, ENDDATE]

		Iterator<String[]> iterator = csvReader.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;

			String[] strings = iterator.next();
//			List<String> items = Arrays.asList(strings);
//			System.out.println(items);
			System.out.printf("Item rownum: %s, memberCode: %s, duration: %s, begin: %s, end: %s %n",
				strings[0], strings[1], strings[2], strings[3], strings[4]);

			if (i == 100) {
				break;
			}
		}

		System.out.println("RecordsRead: " + csvReader.getRecordsRead());
		System.out.println("LinesRead: " + csvReader.getLinesRead());

		csvReader.close();
	}

	/**
	 * Streaming With Apache Commons IO
	 */
	@Test
	public void test01() throws Exception {
		File file = new File("d:\\temp\\BPM00218221.csv");
		LineIterator lineIterator = FileUtils.lineIterator(file, StandardCharsets.UTF_8.displayName());

		int i = 0;
		while (lineIterator.hasNext()) {
			String line = lineIterator.nextLine();
			System.out.println(line);

			i++;
			if (i == 100) {
				break;
			}
		}

		lineIterator.close();
	}
}
