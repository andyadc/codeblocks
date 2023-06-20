package com.andyadc.bms.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Base64;

/**
 * Spring Boot 下读取文件建议使用流(stream)方式
 * <p>
 * https://www.jb51.net/article/219356.htm
 * https://www.jb51.net/article/215018.htm
 * </p>
 */
public final class FileUtil {

	public static byte[] readFilePathToByteArray(String filePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(filePath);
		return FileCopyUtils.copyToByteArray(resource.getInputStream());
	}

	public static String filePath2Base64(String filePath) throws IOException {
		byte[] bytes = readFilePathToByteArray(filePath);
		return Base64.getEncoder().encodeToString(bytes);
	}

	public void test() {
		getClass().getClassLoader().getResourceAsStream("");
		getClass().getResourceAsStream("");
	}

}
