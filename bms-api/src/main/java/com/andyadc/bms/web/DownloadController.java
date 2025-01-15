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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@GetMapping("/download")
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

}
