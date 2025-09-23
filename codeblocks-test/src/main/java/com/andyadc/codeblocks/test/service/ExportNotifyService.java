package com.andyadc.codeblocks.test.service;

public class ExportNotifyService extends AbstractNotifyService<ExportNotifyMessage> {

	@Override
	public void notify(ExportNotifyMessage msg) {

	}

	@Override
	public String notifyType() {
		return "export";
	}
}
