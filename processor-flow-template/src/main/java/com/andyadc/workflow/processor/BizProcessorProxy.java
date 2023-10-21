package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务处理器代理
 */
public class BizProcessorProxy {

	/**
	 * 业务处理器
	 */
	private BizProcessor bizProcessor;

	/**
	 * 下一个工作任务集合
	 */
	private Map<String, String> nextWorkMap = new HashMap<>();

	public void process(ProcessorContext context) throws BizException {
		//业务处理
		if (bizProcessor != null) {
			bizProcessor.process(context);
		}
	}

	public BizProcessor getBizProcessor() {
		return bizProcessor;
	}

	public void setBizProcessor(BizProcessor bizProcessor) {
		this.bizProcessor = bizProcessor;
	}

	public Map<String, String> getNextWorkMap() {
		return nextWorkMap;
	}

	public void setNextWorkMap(Map<String, String> nextWorkMap) {
		this.nextWorkMap = nextWorkMap;
	}
}
