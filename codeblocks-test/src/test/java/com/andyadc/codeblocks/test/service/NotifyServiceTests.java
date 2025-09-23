package com.andyadc.codeblocks.test.service;

public class NotifyServiceTests {

	public static void main(String[] args) throws Exception {
		NotifyService notifyService = new ImportNotifyService();
		notifyService.notify("123");

		notifyService = new ExportNotifyService();
		notifyService.notify("321");
	}
}
