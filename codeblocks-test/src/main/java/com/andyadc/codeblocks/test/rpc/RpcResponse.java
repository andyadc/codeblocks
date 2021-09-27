package com.andyadc.codeblocks.test.rpc;

import java.io.Serializable;

public class RpcResponse implements Serializable {

	// 错误信息
	private Throwable error;
	//  结果集
	private Object result;

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
