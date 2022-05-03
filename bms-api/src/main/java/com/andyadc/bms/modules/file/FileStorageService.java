package com.andyadc.bms.modules.file;

import com.andyadc.bms.modules.file.exception.FileCreateException;
import com.andyadc.bms.modules.file.exception.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FileStorageService {

	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	private static final String separator = File.separator;
	private static final boolean gen_unique_file_name = false;
	private FileStorageSettings fileStorageSettings;

	public static void main(String[] args) {
		String fileCompleteName = "135920.175 (1).png";
		int idx = fileCompleteName.lastIndexOf(" (");
		System.out.println(idx);
		System.out.println(fileCompleteName.substring(0, idx));
		System.out.println(fileCompleteName.substring(idx));
		System.out.println(fileCompleteName.charAt(idx + 2));
	}

	private static String generateShortFileName(String name) {
		return name.substring(0, Math.min(name.length(), 10));
	}

	private static String generateUniqueFileName(String name) {
		name = name.substring(0, Math.min(name.length(), 10));
		String time = LocalTime.now().toString().replace(":", "");
		int i = ThreadLocalRandom.current().nextInt(100, 999);
		return time + "." + i + "." + name;
	}

	private void checkSize(MultipartFile file) {
		Long limit = fileStorageSettings.getMaxSize();
		if (limit != null && limit > 0) {
			long size = file.getSize() / 1024L;
			if (size > limit) {
				throw new FileSizeLimitExceededException("");
			}
		}
	}

	public FileStorageDTO store(MultipartFile file) {
		logger.info("Filename: {}, filesize: {} kb", file.getOriginalFilename(), file.getSize() / 1024F);

		checkSize(file);

		String date = LocalDate.now().toString().replace("-", "");
		String dir = fileStorageSettings.getPath().getPath() + date + separator;
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			boolean mkdirs = dirFile.mkdirs();
			if (!mkdirs) {
				throw new FileCreateException("Can not create file " + dirFile.getName());
			}
		}

		String filename = "";
		String extname = "";

		String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
		int idx = originalFilename.lastIndexOf(".");
		if (idx != -1) {
			filename = originalFilename.substring(0, idx);
			extname = originalFilename.substring(idx);
		}

		String newFilename = generateShortFileName(filename);
		if (gen_unique_file_name) {
			newFilename = generateUniqueFileName(newFilename);
		}
		String dest = dir + newFilename + extname;
		Path path = Paths.get(dest);

		// TODO
		if (!gen_unique_file_name) {
			File nFile = path.toFile();
			boolean ne = nFile.exists();
			if (ne) {
				newFilename = newFilename + " (1)";
				dest = dir + newFilename + extname;
				path = Paths.get(dest);
			}
		}

		try {
			file.transferTo(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileStorageDTO dto = new FileStorageDTO();
		String view = FileStorageConstants.RESOURCE_PATH + date + "/" + newFilename + extname;
		dto.setLocalStoragePath(dir + newFilename + extname);
		dto.setResourcePath(view);

		return dto;
	}

	@Inject
	public void setFileStorageSettings(FileStorageSettings fileStorageSettings) {
		this.fileStorageSettings = fileStorageSettings;
	}
}
