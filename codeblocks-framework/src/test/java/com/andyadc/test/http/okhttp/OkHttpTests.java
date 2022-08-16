package com.andyadc.test.http.okhttp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class OkHttpTests {

	private static final String BASE_URL = "http://localhost:8080/spring-rest";
	private static final String URL_SECURED_BY_BASIC_AUTHENTICATION = "http://browserspy.dk/password-ok.php";

	OkHttpClient client;

	@BeforeEach
	public void init() {
		client = new OkHttpClient();
	}

	@Test
	public void whenUploadFile_thenCorrect() throws IOException {

		RequestBody requestBody = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart(
				"file",
				"file.txt",
				RequestBody.create(
					new File("src/test/resources/test.txt"),
					MediaType.parse("application/octet-stream")
				)
			).build();

		Request request = new Request.Builder()
			.url(BASE_URL + "/upload")
			.post(requestBody)
			.build();

		Call call = client.newCall(request);
		Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenGetUploadFileProgress_thenCorrect() throws IOException {

		final RequestBody requestBody = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart(
				"file",
				"file.txt",
				RequestBody.create(
					new File("src/test/resources/test.txt"),
					MediaType.parse("application/octet-stream")
				)
			).build();

		final ProgressRequestWrapper countingBody = new ProgressRequestWrapper(
			requestBody,
			(long bytesWritten, long contentLength) -> {
				final float percentage = (100f * bytesWritten) / contentLength;

				Assertions.assertFalse(Float.compare(percentage, 100) > 0);
			}
		);

		final Request request = new Request.Builder()
			.url(BASE_URL + "/upload")
			.post(countingBody)
			.build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenSendPostRequest_thenCorrect() throws IOException {

		final RequestBody formBody = new FormBody.Builder()
			.add("username", "test")
			.add("password", "test")
			.build();

		final Request request = new Request.Builder()
			.url(BASE_URL + "/users")
			.post(formBody)
			.build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenSendPostRequestWithAuthorization_thenCorrect() throws IOException {

		final String postBody = "test post";

		final Request request = new Request.Builder()
			.url(URL_SECURED_BY_BASIC_AUTHENTICATION)
			.addHeader("Authorization",
				Credentials.basic("test", "test")
			)
			.post(RequestBody.create(
				postBody,
				MediaType.parse("text/x-markdown")
				)
			)
			.build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenPostJson_thenCorrect() throws IOException {

		final String json = "{\"id\":1,\"name\":\"John\"}";

		final RequestBody body = RequestBody.create(
			json,
			MediaType.parse("application/json")
		);

		final Request request = new Request.Builder().url(BASE_URL + "/users/detail").post(body).build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenPostJsonWithoutCharset_thenCharsetIsUtf8() {

		final String json = "{\"id\":1,\"name\":\"John\"}";

		final RequestBody body = RequestBody.create(
			json,
			MediaType.parse("application/json")
		);

		String charset = body.contentType().charset().displayName();

		Assertions.assertEquals(charset, "UTF-8");
	}

	@Test
	public void whenPostJsonWithUtf16Charset_thenCharsetIsUtf16() {

		final String json = "{\"id\":1,\"name\":\"John\"}";

		final RequestBody body = RequestBody.create(
			json,
			MediaType.parse("application/json; charset=utf-16")
		);

		String charset = body.contentType().charset().displayName();

		Assertions.assertEquals(charset, "UTF-16");
	}

	@Test
	public void whenSendMultipartRequest_thenCorrect() throws IOException {

		final RequestBody requestBody = new MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("username", "test")
			.addFormDataPart("password", "test")
			.addFormDataPart("file", "file.txt",
				RequestBody.create(
					new File("src/test/resources/test.txt"),
					MediaType.parse("application/octet-stream")
				)
			).build();

		final Request request = new Request.Builder().url(BASE_URL + "/users/multipart").post(requestBody).build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenGetRequest_thenCorrect() throws IOException {

		final Request request = new Request.Builder().url(BASE_URL + "/date").build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenGetRequestWithQueryParameter_thenCorrect() throws IOException {

		final HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/ex/bars").newBuilder();
		urlBuilder.addQueryParameter("id", "1");

		final String url = urlBuilder.build().toString();

		final Request request = new Request.Builder().url(url).build();

		final Call call = client.newCall(request);
		final Response response = call.execute();

		Assertions.assertEquals(response.code(), 200);
	}

	@Test
	public void whenAsynchronousGetRequest_thenCorrect() throws InterruptedException {

		final Request request = new Request.Builder().url(BASE_URL + "/date").build();

		final Call call = client.newCall(request);

		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				System.out.println("OK");
			}

			@Override
			public void onFailure(Call call, IOException e) {
				fail();
			}
		});

		Thread.sleep(3000L);
	}
}
