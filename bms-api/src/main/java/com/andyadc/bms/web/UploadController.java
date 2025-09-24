package com.andyadc.bms.web;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.modules.file.FileStorageDTO;
import com.andyadc.bms.modules.file.FileStorageService;
import com.andyadc.bms.web.dto.UploadRequest;
import jakarta.inject.Inject;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	// 文件存储目录
	private static final String UPLOAD_DIR = "D://temp//uploads/";
	private final FileStorageService fileStorageService;

	@Inject
	public UploadController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@PostMapping("/upload/1")
	public Object upload(UploadRequest request,
						 @RequestParam("file") MultipartFile file) {
		if (file == null || file.isEmpty()) {
			logger.error("Upload file is null.");
			return ResponseEntity.ok("Upload fail");
		}

		FileStorageDTO store = fileStorageService.store(file);
		return ResponseEntity.ok(Response.of(RespCode.SUCC, store));
	}

	@GetMapping("/upload/native")
	public String upload_native() {
		return "upload/upload-native";
	}

	@GetMapping("/upload/bootstrap")
	public String upload_bootstrap() {
		return "upload/upload-bootstrap";
	}

	@GetMapping("/upload/dropzone")
	public String upload_dropzone() {
		return "upload/upload-dropzone";
	}

	@GetMapping("/upload/uppy")
	public String upload_uppy() {
		return "upload/upload-uppy";
	}

	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> handleFileUpload(
		@RequestParam("name") String name,
		@RequestParam("email") String email,
		@RequestParam("file") MultipartFile file) {
		logger.info("name: {}, email: {}, file: {}", name, email, file != null ? file.getSize() : "null");

		Map<String, String> response = new HashMap<>();

		try {
			// 验证文件是否为空
			if (file.isEmpty()) {
				response.put("message", "请选择一个文件上传");
				return ResponseEntity.badRequest().body(response);
			}

			// 在handleFileUpload方法中添加
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
			if (!Arrays.asList("jpg", "png", "pdf", "docx").contains(fileExtension.toLowerCase())) {
				response.put("message", "不支持的文件类型");
//				return ResponseEntity.badRequest().body(response);
			}

			// 创建上传目录(如果不存在)
			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			// 生成唯一的文件名
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

			// 保存文件
			File dest = new File(UPLOAD_DIR + fileName);
			file.transferTo(dest);

			// 这里可以添加业务逻辑，如保存到数据库等
			// saveToDatabase(name, email, fileName);

			response.put("message", "文件上传成功: " + fileName);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "文件上传失败: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
