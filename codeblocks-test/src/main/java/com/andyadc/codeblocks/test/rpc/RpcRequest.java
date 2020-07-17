package com.andyadc.codeblocks.test.rpc;

import java.io.Serializable;

public class RpcRequest implements Serializable {

	// 全限定类名
	private String className;
	// 方法名称
	private String methodName;
	//  参数类型
	private Class<?>[] paramsType;
	// 参数具体的值
	private Object[] params;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParamsType() {
		return paramsType;
	}

	public void setParamsType(Class<?>[] paramsType) {
		this.paramsType = paramsType;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}
