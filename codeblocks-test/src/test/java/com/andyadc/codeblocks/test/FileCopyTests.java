package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.StopWatch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileCopyTests {

	private static final String SRC_FILE_PATH = "D:/src.txt";
	private static final String DEST_FILE_PATH = "D:/dest.txt";

	public static void main(String[] args) throws Exception {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start("init");
		init();
		stopWatch.stop();

//		stopWatch.start("perByteOperation");
//		perByteOperation();
//		stopWatch.stop();

		stopWatch.start("bufferOperationWith100Buffer");
		bufferOperationWith100Buffer();
		stopWatch.stop();

		stopWatch.start("largerBufferOperation");
		largerBufferOperation();
		stopWatch.stop();

		stopWatch.start("bufferedStreamByteOperation");
		bufferedStreamByteOperation();
		stopWatch.stop();

		stopWatch.start("bufferedStreamBufferOperation");
		bufferedStreamBufferOperation();
		stopWatch.stop();

		stopWatch.start("fileChannelOperation");
		fileChannelOperation();
		stopWatch.stop();

		System.out.println(stopWatch.prettyPrint());
	}

	private static void init() throws Exception {
		Files.write(Paths.get(
			SRC_FILE_PATH),
			IntStream.rangeClosed(1, 1000000).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()),
			StandardCharsets.UTF_8,
			StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static void perByteOperation() throws Exception {
		String dest_path = DEST_FILE_PATH + "-perByteOperation";
		Files.deleteIfExists(Paths.get(dest_path));

		try (FileInputStream inputStream = new FileInputStream(SRC_FILE_PATH)) {
			FileOutputStream outputStream = new FileOutputStream(dest_path);
			int i;
			while ((i = inputStream.read()) != -1) {
				outputStream.write(i);
			}
		}
	}

	private static void bufferOperationWith100Buffer() throws Exception {
		String dest_path = DEST_FILE_PATH + "-bufferOperationWith100Buffer";
		Files.deleteIfExists(Paths.get(dest_path));

		try (FileInputStream inputStream = new FileInputStream(SRC_FILE_PATH)) {
			FileOutputStream outputStream = new FileOutputStream(dest_path);
			byte[] buffer = new byte[100];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		}
	}

	private static void largerBufferOperation() throws Exception {
		String dest_path = DEST_FILE_PATH + "-largerBufferOperation";
		Files.deleteIfExists(Paths.get(dest_path));

		try (FileInputStream inputStream = new FileInputStream(SRC_FILE_PATH)) {
			FileOutputStream outputStream = new FileOutputStream(dest_path);
			byte[] buffer = new byte[8192];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		}
	}

	private static void bufferedStreamByteOperation() throws Exception {
		String dest_path = DEST_FILE_PATH + "-bufferedStreamByteOperation";
		Files.deleteIfExists(Paths.get(dest_path));

		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(SRC_FILE_PATH))) {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dest_path));
			int i;
			while ((i = bufferedInputStream.read()) != -1) {
				bufferedOutputStream.write(i);
			}
		}
	}

	private static void bufferedStreamBufferOperation() throws Exception {
		String dest_path = DEST_FILE_PATH + "-bufferedStreamBufferOperation";
		Files.deleteIfExists(Paths.get(dest_path));

		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(SRC_FILE_PATH))) {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dest_path));
			byte[] buffer = new byte[8192];
			int len;
			while ((len = bufferedInputStream.read(buffer)) != -1) {
				bufferedOutputStream.write(buffer, 0, len);
			}
		}
	}

	private static void fileChannelOperation() throws Exception {
		String dest_path = DEST_FILE_PATH + "-fileChannelOperation";
		Files.deleteIfExists(Paths.get(dest_path));

		FileChannel in = FileChannel.open(Paths.get(SRC_FILE_PATH), StandardOpenOption.READ);
		FileChannel out = FileChannel.open(Paths.get(dest_path), StandardOpenOption.CREATE, StandardOpenOption.WRITE);

		in.transferTo(0, in.size(), out);
		in.close();
	}

}
