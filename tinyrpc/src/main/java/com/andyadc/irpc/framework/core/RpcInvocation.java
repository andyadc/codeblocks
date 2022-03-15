package com.andyadc.irpc.framework.core;

import java.util.Arrays;
import java.util.StringJoiner;

public class RpcInvocation {

	private String targetMethod;

	private String targetServiceName;

	private Object[] args;

	private String uid;

	private Object response;

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public String getTargetServiceName() {
		return targetServiceName;
	}

	public void setTargetServiceName(String targetServiceName) {
		this.targetServiceName = targetServiceName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RpcInvocation.class.getSimpleName() + "[", "]")
			.add("targetMethod=" + targetMethod)
			.add("targetServiceName=" + targetServiceName)
			.add("args=" + Arrays.toString(args))
			.add("uid=" + uid)
			.add("response=" + response)
			.toString();
	}
}
