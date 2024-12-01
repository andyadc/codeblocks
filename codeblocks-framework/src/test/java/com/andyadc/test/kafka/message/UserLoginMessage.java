package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;

public class UserLoginMessage extends Message<UserLoginBody> {

	private UserLoginBody body;

	@Override
	public UserLoginBody getBody() {
		return body;
	}

	@Override
	public void setBody(UserLoginBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "UserLoginMessage{" +
			"body=" + body +
			"} " + super.toString();
	}
}
