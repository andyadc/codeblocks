package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;

public class UserRegisterMessage extends Message<UserRegisterBody> {

	private UserRegisterBody body;

	@Override
	public UserRegisterBody getBody() {
		return body;
	}

	@Override
	public void setBody(UserRegisterBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "UserRegisterMessage{" +
			"body=" + body +
			"} " + super.toString();
	}

}
