package com.andyadc.codeblocks.test.asm;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class Monitor {

	public static void main(String[] args) throws Exception {
		File file = new File("123");
		FileUtils.writeByteArrayToFile(file, new byte[]{1, 2, 3});
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getPath());
	}

	public String look(String see) {
		System.out.println("I'am looking...");
		return UUID.randomUUID().toString();
	}
}
