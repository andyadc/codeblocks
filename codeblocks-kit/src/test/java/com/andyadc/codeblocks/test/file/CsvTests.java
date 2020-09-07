package com.andyadc.codeblocks.test.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class CsvTests {

	public static void main(String[] args) throws Exception {
		testCsv();
	}

	private static void testCsv() throws IOException {
		/*
		  utf-8的bom头
		 */
		byte[] UTF8_HEADER_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

		String csvFileName = "D:\\temp\\test.csv";
		FileUtils.deleteQuietly(new File(csvFileName));

		RandomAccessFile file = new RandomAccessFile(csvFileName, "rw");
		FileChannel channel = file.getChannel();


		byte[] header = "编号,品名,时间戳\n".getBytes(StandardCharsets.UTF_8);

		//写入utf8的bom头，防止打开csv时显示乱码
		MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, file.length(), UTF8_HEADER_BOM.length);
		mappedByteBuffer.put(UTF8_HEADER_BOM);

		//写入标题栏
		mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, file.length(), header.length);
		mappedByteBuffer.put(header);

		//分批写入记录(每批1000条)-防止OOM
		long timestamp = System.currentTimeMillis();
		for (int i = 1; i <= 100; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 1; j <= 1000; j++) {
				sb.append(i * j + "\t,");
				sb.append("产品" + j + ",");
				sb.append(timestamp + "\t\n");
			}
			byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
			mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, file.length(), data.length);
			mappedByteBuffer.put(data);
		}

		//关闭通道
		channel.close();
	}
}
