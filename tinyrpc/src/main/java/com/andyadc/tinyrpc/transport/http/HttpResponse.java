package com.andyadc.tinyrpc.transport.http;

public class HttpResponse extends DefaultHttpResponseMessage {

	public HttpResponse() {
	}

	public HttpResponse(int status, HttpHeaders httpHeaders) {
		this(status, httpHeaders, new byte[0]);
	}

	public HttpResponse(int status, HttpHeaders httpHeaders, byte[] content) {
		this.status = status;
		this.httpHeaders = httpHeaders;
		this.content = content == null ? new byte[0] : content;
	}
}
