package com.andyadc.bms.web;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.modules.file.FileStorageDTO;
import com.andyadc.bms.modules.file.FileStorageService;
import com.andyadc.bms.web.dto.UploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

@RequestMapping("/")
@RestController
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final FileStorageService fileStorageService;

	@Inject
	public UploadController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@PostMapping("/upload")
	public Object upload(UploadRequest request,
						 @RequestParam("file") MultipartFile file) {
		if (file == null || file.isEmpty()) {
			logger.error("Upload file is null.");
			return ResponseEntity.ok("Upload fail");
		}

		FileStorageDTO store = fileStorageService.store(file);
		return ResponseEntity.ok(Response.of(RespCode.SUCC, store));
	}

}
