package com.andyadc.test.http;

import com.andyadc.codeblocks.framework.http.HttpClientConfiguration;
import com.andyadc.codeblocks.framework.http.HttpComponentsClientBuilder;
import com.andyadc.codeblocks.framework.http.HttpRequestException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpComponentsClientTest {

	protected static HttpClientConfiguration configuration = HttpClientConfiguration.common();
	private static CloseableHttpClient httpClient;

	@BeforeAll
	public static void startup() {
		httpClient = HttpComponentsClientBuilder.build(configuration);
	}

	private static String createHttpInfo(PoolingHttpClientConnectionManager connectionManager) {
		StringBuilder sb = new StringBuilder();
		sb.append("=========================").append("\n");
		sb.append("General Info:").append("\n");
		sb.append("-------------------------").append("\n");
		sb.append("MaxTotal: ").append(connectionManager.getMaxTotal()).append("\n");
		sb.append("DefaultMaxPerRoute: ").append(connectionManager.getDefaultMaxPerRoute()).append("\n");
		sb.append("ValidateAfterInactivity: ").append(connectionManager.getValidateAfterInactivity()).append("\n");
		sb.append("=========================").append("\n");

		PoolStats totalStats = connectionManager.getTotalStats();
		sb.append(createPoolStatsInfo("Total Stats", totalStats));

		Set<HttpRoute> routes = connectionManager.getRoutes();

		if (routes != null) {
			for (HttpRoute route : routes) {
				sb.append(createRouteInfo(connectionManager, route));
			}
		}

		return sb.toString();
	}

	private static String createRouteInfo(
		PoolingHttpClientConnectionManager connectionManager, HttpRoute route) {
		PoolStats routeStats = connectionManager.getStats(route);
		return createPoolStatsInfo(route.getTargetHost().toURI(), routeStats);
	}

	private static String createPoolStatsInfo(String title, PoolStats poolStats) {
		StringBuilder sb = new StringBuilder();

		sb.append(title).append(":").append("\n");
		sb.append("-------------------------").append("\n");

		if (poolStats != null) {
			sb.append("Available: ").append(poolStats.getAvailable()).append("\n");
			sb.append("Leased: ").append(poolStats.getLeased()).append("\n");
			sb.append("Max: ").append(poolStats.getMax()).append("\n");
			sb.append("Pending: ").append(poolStats.getPending()).append("\n");
		}

		sb.append("=========================").append("\n");

		return sb.toString();
	}

	// Method to extract the filename from Content-Disposition header
	private static String parseFileNameFromDisposition(String contentDisposition) {
		// Regex pattern to extract filename from the Content-Disposition header
		Pattern pattern = Pattern.compile("filename=\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(contentDisposition);

		if (matcher.find()) {
			return matcher.group(1); // Return the filename found
		}
		return null; // No filename found
	}

	// Method to add a unique suffix to the filename (e.g., filename.txt, filename_20250109_160611.txt)
	private static String addUniqueSuffix(String filename, String targetDir) {
		// Check if file already exists, if so, add timestamp to avoid overwriting
		File file = new File(targetDir + "/" + filename);
		if (!file.exists()) {
			return filename;
		}

		// Add timestamp to the filename to make it unique
		String filenameWithoutExtension = filename.contains(".") ? filename.substring(0, filename.lastIndexOf(".")) : filename;
		String extension = filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return filenameWithoutExtension + "_" + timestamp + extension;
	}

	public static String addNumberedSuffix(String filename, String targetDir) {
		int counter = 1;
		String newFileName = filename;
		File file = new File(newFileName);

		while (file.exists()) {
			// Split the filename into name and extension
			String baseName = filename.contains(".") ? filename.substring(0, filename.lastIndexOf(".")) : filename;
			String extension = filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";

			// Create a new file name with a number in parentheses (file(1).jpg, file(2).jpg, etc.)
			newFileName = baseName + "(" + counter + ")" + extension;
			file = new File(newFileName);
			counter++;
		}

		return newFileName;
	}

	// (e.g., /path/filename.txt)
	private static String genFullPath(String filename, String targetDir) {
		return targetDir + "/" + addUniqueSuffix(filename, targetDir);
	}

	// Main method to download a file from a given URL
	// https://example.com/file.jpg
	private String parseFilename(String fileUrl) {
		// Fallback: Extract filename from URL
		String[] urlParts = fileUrl.split("/");
		return urlParts[urlParts.length - 1];
	}

	@Test
	public void pathfile() throws IOException {
		String targetDir = "/temp/download";
		Path targetDirectory = Paths.get(targetDir);
		System.out.println(targetDirectory);
		System.out.println(Files.exists(targetDirectory));

		String filename = "test";
		Path targetFilePath = targetDirectory.resolve(filename);
		System.out.println(targetFilePath);

		InputStream inputStream = null;
		Files.copy(inputStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
	}

	@Test
	public void testDownload() throws Exception {
		String url = "http://localhost:8080/download";
		String targetDir = "/temp/download"; // Destination for file download

		RequestBuilder requestBuilder = RequestBuilder.get(url);
		requestBuilder.setCharset(StandardCharsets.UTF_8);
		requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF8");

		// parameters
		Map<String, String> parameters = new HashMap<>();
		parameters.put("", "");

		requestBuilder.addParameters(parameters.entrySet().stream().map(
			entry -> new BasicNameValuePair(entry.getKey(), entry.getValue() == null ? "" : entry.getValue())
		).toArray(NameValuePair[]::new));

		// headers
		Map<String, String> headers = new HashMap<>();
		headers.put("", "");

		headers.forEach((key, value) -> {
			if (key != null && !key.isEmpty()) {
				requestBuilder.addHeader(key, value != null ? value : "");
			}
		});

		HttpUriRequest request = requestBuilder.build();

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) { // 200
				request.abort();
				throw new HttpRequestException(statusCode, String.format(
					"HttpClient HTTP request failed with code: %d, reason: %s",
					statusCode,
					statusLine.getReasonPhrase()
				));
			}

			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return;
			}

			Header[] allHeaders = response.getAllHeaders();
			for (Header header : allHeaders) {
				System.out.println(header.getName() + " - " + header.getValue());
			}

			Header contentDispositionHeader = response.getFirstHeader("Content-Disposition");
			Header contentTypeHeader = response.getFirstHeader("Content-Type");
			// Check Content-Type to determine response type
			String contentType = contentTypeHeader != null ? contentTypeHeader.getValue() : "";
			if (contentType.contains("text") || contentType.contains("json") || contentType.contains("html")) {
				// Handle as text response (e.g., JSON, HTML, plain text)
				String responseText = EntityUtils.toString(entity);
				System.out.println("Text Response: " + responseText);
			} else {
				// Handle as binary response (e.g., file download)
				String filename = null;
				if (contentDispositionHeader != null) {
					String disposition = contentDispositionHeader.getValue();
					filename = parseFileNameFromDisposition(disposition);
				}
				if (filename == null) {
					filename = "downloaded_file"; // default filename
				}
				// Ensure unique filename by appending a timestamp
				String uniqueFileName = addUniqueSuffix(filename, targetDir);

				String fullPath = genFullPath(uniqueFileName, targetDir);

				try (InputStream inputStream = entity.getContent();
					 OutputStream outputStream = new FileOutputStream(fullPath)) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					System.out.println("File downloaded successfully to " + targetDir);
				}

				EntityUtils.consume(entity); // Ensure response entity is fully consumed

//				try (InputStream inputStream = entity.getContent()) {
//					Path targetDirectory = Paths.get(targetDir);
//					Path targetFilePath = targetDirectory.resolve(uniqueFileName);
//					Files.copy(inputStream, targetFilePath);
//				}


			}

			// Avoid EntityUtils.toString for Large Responses
			// Use InputStream for streaming large responses to avoid high memory usage.
			// <code> try (InputStream inputStream = entity.getContent()) { // Process the stream } </code>
//			try (InputStream content = entity.getContent()) {
//				// For large responses, use buffered reading
//				if (entity.getContentLength() > 1024 * 1024) { // If content is larger than 1MB
//					processLargeContent(content);
//				}
//				EntityUtils.toString(entity, charset);
//			} finally {
//				EntityUtils.consumeQuietly(entity);
//			}

		}
	}

	/**
	 * Use InputStream for streaming large responses to avoid high memory usage.
	 */
	private String processLargeContent(InputStream content) throws IOException {
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8))) {
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer)) != -1) {
				result.append(buffer, 0, read);
			}
		}
		return result.toString();
	}

}
