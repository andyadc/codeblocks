package com.andyadc.workflow.processor;

import com.andyadc.workflow.ProcessorConstant;

public class ProcessorContext {

	/**
	 * 上下文标示
	 */
	private String contextId;

	/**
	 * 子上下文对象的标示
	 */
	private String subContextId;

	/**
	 * 工作流整合类型
	 */
	private String integrationChannelType;

	/**
	 * 当前工作状态
	 */
	private String currentWork = ProcessorConstant.WORK_START;

	/**
	 * 处理结果
	 */
	private String processResult = ProcessorConstant.PROCESS_CONTINUE;

	public String getCurrentWork() {
		return currentWork;
	}

	public void setCurrentWork(String currentWork) {
		this.currentWork = currentWork;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getSubContextId() {
		return subContextId;
	}

	public void setSubContextId(String subContextId) {
		this.subContextId = subContextId;
	}

	public String getIntegrationChannelType() {
		return integrationChannelType;
	}

	public void setIntegrationChannelType(String integrationChannelType) {
		this.integrationChannelType = integrationChannelType;
	}
}
