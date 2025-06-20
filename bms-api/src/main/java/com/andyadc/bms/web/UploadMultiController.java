package com.andyadc.bms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
public class UploadMultiController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	// 文件存储目录
	private static final String UPLOAD_DIR = "D://temp//uploads/";

	@PostMapping("/submit-form")
	public ResponseEntity<Map<String, String>> handleFormUpload(
		@RequestParam("name") String name,
		@RequestParam("email") String email,
		@RequestParam(value = "phone", required = false) String phone,
		@RequestParam(value = "files", required = false) MultipartFile[] files) {

		Map<String, String> response = new HashMap<>();

		try {
			// 1. 验证表单数据
			if (name == null || name.trim().isEmpty()) {
				response.put("message", "姓名不能为空");
				return ResponseEntity.badRequest().body(response);
			}

			if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
				response.put("message", "请输入有效的邮箱地址");
				return ResponseEntity.badRequest().body(response);
			}

			// 2. 处理文件上传
			List<String> uploadedFiles = new ArrayList<>();
			if (files != null && files.length > 0) {
				// 创建上传目录(如果不存在)
				File uploadDir = new File(UPLOAD_DIR);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				for (MultipartFile file : files) {
					if (file.isEmpty()) continue;

					// 生成安全文件名
					String fileName = generateSafeFileName(file.getOriginalFilename());

					// 保存文件
					File dest = new File(UPLOAD_DIR + fileName);
					file.transferTo(dest);

					uploadedFiles.add(fileName);
				}
			}

			// 3. 这里可以添加业务逻辑，如保存到数据库等
			// saveToDatabase(userName, userEmail, userPhone, uploadedFiles);

			// 4. 返回成功响应
			response.put("message", "表单提交成功");
			response.put("userName", name);
			response.put("userEmail", email);
			if (phone != null) response.put("userPhone", phone);
			if (!uploadedFiles.isEmpty()) response.put("files", String.join(", ", uploadedFiles));

			return ResponseEntity.ok(response);
		} catch (IOException e) {
			response.put("message", "文件上传失败: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (Exception e) {
			response.put("message", "表单提交失败: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private String generateSafeFileName(String originalName) {
		String safeName = originalName.replaceAll("[^a-zA-Z0-9.-]", "_");
		return System.currentTimeMillis() + "_" + safeName;
	}
}
