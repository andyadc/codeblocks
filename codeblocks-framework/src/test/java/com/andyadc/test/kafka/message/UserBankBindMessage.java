package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;

public class UserBankBindMessage extends Message<UserBankBindBody> {

	private UserBankBindBody body;

	@Override
	public UserBankBindBody getBody() {
		return body;
	}

	@Override
	public void setBody(UserBankBindBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "UserBankBindMessage{" +
			"body=" + body +
			"} " + super.toString();
	}

}
