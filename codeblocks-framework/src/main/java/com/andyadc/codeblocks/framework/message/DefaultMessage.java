package com.andyadc.codeblocks.framework.message;

public class DefaultMessage extends Message<String> {

	private String body;

	@Override
	public String getBody() {
		return this.body;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "{" +
			"body=" + body +
			"} " + super.toString();
	}

}
