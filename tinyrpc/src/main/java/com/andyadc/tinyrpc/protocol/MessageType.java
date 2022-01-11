package com.andyadc.tinyrpc.protocol;

public enum MessageType {

	;

	/**
	 * 类型
	 */
	private final byte type;
	/**
	 * 是否是请求
	 */
	private final boolean request;

	MessageType(byte type, boolean request) {
		this.type = type;
		this.request = request;
	}

	/**
	 * 获取消息类型
	 *
	 * @param type 类型
	 * @return 消息类型
	 */
	public static MessageType valueOf(final int type) {

		return null;
	}
}
