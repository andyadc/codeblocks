package com.andyadc.codeblocks.framework.http;

import java.io.IOException;

/**
 * andy.an
 * 2019/12/6
 */
public interface Lifecycle {

	void init();

	void close() throws IOException;
}
