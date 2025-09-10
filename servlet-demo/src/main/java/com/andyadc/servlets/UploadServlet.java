//package com.andyadc.servlets;
//
//import com.andyadc.Constants;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(
//	name = "uploadServlet",
//	urlPatterns = {"/uploadFile"}
//)
//public class UploadServlet extends HttpServlet {
//
//	private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		if (ServletFileUpload.isMultipartContent(request)) {
//			DiskFileItemFactory factory = new DiskFileItemFactory();
//			factory.setSizeThreshold(Constants.MEMORY_THRESHOLD);
//			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
//
//			ServletFileUpload upload = new ServletFileUpload(factory);
//			upload.setFileSizeMax(Constants.MAX_FILE_SIZE);
//			upload.setSizeMax(Constants.MAX_REQUEST_SIZE);
//
//			String uploadPath = getServletContext().getRealPath("") + File.separator + Constants.UPLOAD_DIRECTORY;
//			File uploadDir = new File(uploadPath);
//			if (!uploadDir.exists()) {
//				uploadDir.mkdir();
//			}
//
//			try {
//				List<FileItem> formItems = upload.parseRequest(request);
//				if (formItems != null && formItems.size() > 0) {
//					for (FileItem item : formItems) {
//						if (!item.isFormField()) {
//							String fileName = new File(item.getName()).getName();
//							String filePath = uploadPath + File.separator + fileName;
//							logger.info("- {}", filePath);
//							File storeFile = new File(filePath);
//							item.write(storeFile);
//							request.setAttribute("message", "File " + fileName + " has uploaded successfully!");
//						}
//					}
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				request.setAttribute("message", "There was an error: " + ex.getMessage());
//			}
//			getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
//		}
//	}
//}
