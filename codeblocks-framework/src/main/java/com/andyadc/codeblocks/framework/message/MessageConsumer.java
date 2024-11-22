package com.andyadc.codeblocks.framework.message;

public interface MessageConsumer {

	void consume(Message<String> message);
}
