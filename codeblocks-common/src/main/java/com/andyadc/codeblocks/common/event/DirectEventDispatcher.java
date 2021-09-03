package com.andyadc.codeblocks.common.event;

public class DirectEventDispatcher extends AbstractEventDispatcher {

	public static final String NAME = "direct";

	public DirectEventDispatcher() {
		super(DIRECT_EXECUTOR);
	}
}
