package com.andyadc.workflow.processor;

import com.andyadc.workflow.base.ProcessorConstant;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessorContext {

	/**
	 * 内部业务请求对象
	 */
	public static final String TRANSACTION_FLOW_REQUEST = "transaction_flow_request";

	/**
	 * 请求对象
	 */
	public static final String ORDER_REQUEST = "order_request";

	/**
	 * 响应对象
	 */
	public static final String ORDER_RESPONSE = "order_response";

	/**
	 * 自定义的交易类型
	 */
	public static final String BIZ_TYPE = "biz_type";
	private final Map<String, Object> variables = new ConcurrentHashMap<>();
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

	private String errorCode;
	private String errorMessage;

	/**
	 * 当前工作状态
	 */
	private String currentWork = ProcessorConstant.WORK_START;

	/**
	 * 处理结果
	 */
	private String processResult = ProcessorConstant.PROCESS_CONTINUE;

	public void delVariables(String key) {
		getVariables().remove(key);
	}

	public void setVariables(String key, Object object) {
		if (StringUtils.hasText(key) && object != null) {
			getVariables().put(key, object);
		}
	}

	public <T> T getVarValue(String key, Class<T> clazz) {
		Object o = variables.get(key);
		if (clazz.isInstance(o)) {
			return (T) o;
		}
		return null;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

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

	public String getBizType() {
		return getVarValue(BIZ_TYPE, String.class);
	}

	public void setBizType(String bizType) {
		setVariables(BIZ_TYPE, bizType);
	}

	@Override
	public String toString() {
		return "ProcessorContext{" +
			"contextId=" + contextId +
			", subContextId=" + subContextId +
			", integrationChannelType=" + integrationChannelType +
			", variables[" + !variables.isEmpty() + "]" +
			'}';
	}
}
