package com.andyadc.codeblocks.test.service;

public class ImportNotifyService extends AbstractNotifyService<ImportNotifyMessage> {

	@Override
	public void notify(ImportNotifyMessage msg) {

	}

	@Override
	public String notifyType() {
		return "import";
	}
}
