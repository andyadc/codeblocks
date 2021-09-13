package com.andyadc.codeblocks.test.file;

import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class CsvFileRead {

	public static void main(String[] args) {
		System.out.println(StandardCharsets.UTF_8.displayName());

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
