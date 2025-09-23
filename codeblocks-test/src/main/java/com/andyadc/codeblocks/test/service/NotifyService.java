package com.andyadc.codeblocks.test.service;

public interface NotifyService {

	String notifyType();

	void notify(String message) throws Exception;
}
