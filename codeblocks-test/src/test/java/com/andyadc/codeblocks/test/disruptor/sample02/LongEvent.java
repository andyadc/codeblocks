package com.andyadc.codeblocks.test.disruptor.sample02;

public class LongEvent {

	private long value;

	public void set(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LongEvent{" + "value=" + value + '}';
	}
}
