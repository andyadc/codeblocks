package com.andyadc.codeblocks.test.disruptor;

import com.lmax.disruptor.EventFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class ValueEvent {

	public final static EventFactory<ValueEvent> EVENT_FACTORY = ValueEvent::new;

	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
