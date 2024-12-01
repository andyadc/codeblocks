package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;

public class UserBankAuthMessage extends Message<UserBankAuthBody> {

	private UserBankAuthBody body;

	@Override
	public UserBankAuthBody getBody() {
		return body;
	}

	@Override
	public void setBody(UserBankAuthBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "UserBankAuthMessage{" +
			"body=" + body +
			"} " + super.toString();
	}
}
