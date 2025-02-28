package com.andyadc.bms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class DownloadController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 大文件流式下载
	 */
	@RequestMapping("/download")
	public ResponseEntity<Resource> download() throws Exception {
		String filePath = "D:/temp/" + "test.txt";

		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			InputStream inputStream = new FileInputStream(file);

			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl("no-cache, no-store, must-revalidate");
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
				ContentDisposition.builder("attachment").filename(file.getName()).build()
			);
			headers.setContentLength(file.length());

			return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// TODO
	@RequestMapping("/download2")
	public ResponseEntity<byte[]> download2() throws Exception {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] fileBytes = stream.toByteArray();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "file.name");
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length));

		// 返回文件字节数组
		return ResponseEntity.ok()
			.headers(headers)
			.body(fileBytes);
	}

}
