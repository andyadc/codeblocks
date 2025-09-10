package com.andyadc.servlets;

import com.andyadc.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@WebServlet(name = "multipartServlet", value = "/multipartServlet")
@MultipartConfig(
	location = "D://",
	fileSizeThreshold = 1024 * 1024,
	maxFileSize = 1024 * 1024 * 5,
	maxRequestSize = 1024 * 1024 * 5 * 5
)
public class MultipartServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(MultipartServlet.class);

	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
			}
		}
		return Constants.DEFAULT_FILENAME;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadPath = getServletContext().getRealPath("") + File.separator + Constants.UPLOAD_DIRECTORY;
		logger.info("uploadPath: {}", uploadPath);

		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			if (!uploadDir.mkdir()) {
				logger.error("mkdir error");
				return;
			}
		}
		try {
			String fileName = "";
			for (Part part : request.getParts()) {
				fileName = getFileName(part);

				String toPath = uploadPath + File.separator + fileName;
				logger.info("toPath: {}", toPath);
				part.write(toPath);
//				part.write(File.separator + fileName);
			}

			request.setAttribute("message", "File " + fileName + " has uploaded successfully!");
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
			request.setAttribute("message", "There was an error: " + fne.getMessage());
		}
		getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
	}
}
