package com.andyadc.codeblocks.framework.http;

import java.io.IOException;

public interface Lifecycle {

	void init();

	void close() throws IOException;
}
