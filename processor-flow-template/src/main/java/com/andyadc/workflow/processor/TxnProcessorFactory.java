package com.andyadc.workflow.processor;

import com.andyadc.workflow.processor.core.BizProcessor;

import java.util.HashMap;
import java.util.Map;

public class TxnProcessorFactory {

	//特殊交易请求处理器集合
	protected Map<String, BizProcessor> txnRequestProcessorMap = new HashMap<>();
	//特殊交易返回处理器集合
	protected Map<String, BizProcessor> txnResponseProcessorMap = new HashMap<>();
	//特殊异常处理器集合
	protected Map<String, BizProcessor> exceptionProcessorMap = new HashMap<>();

	//默认交易处理器
	protected BizProcessor defaultTxnRequestProcessor;
	//默认交易返回处理器
	protected BizProcessor defaultTxnResponseProcessor;
	//默认异常处理器
	protected BizProcessor defaultExceptionProcessor;

	public BizProcessor getTxnRequestProcessor(ProcessorContext context) {
		BizProcessor bizProcessor = txnRequestProcessorMap.get(context.getBizType());
		if (bizProcessor == null) {
			return defaultTxnRequestProcessor;
		}
		return bizProcessor;
	}

	public BizProcessor getTxnResponseProcessor(ProcessorContext context) {
		BizProcessor bizProcessor = txnResponseProcessorMap.get(context.getBizType());
		if (bizProcessor == null) {
			return defaultTxnResponseProcessor;
		}
		return bizProcessor;
	}

	public BizProcessor getExceptionProcessor(ProcessorContext context) {
		BizProcessor bizProcessor = exceptionProcessorMap.get(context.getIntegrationChannelType());
		if (bizProcessor == null) {
			return defaultExceptionProcessor;
		}
		return bizProcessor;
	}

	public void setTxnRequestProcessorMap(Map<String, BizProcessor> txnRequestProcessorMap) {
		this.txnRequestProcessorMap = txnRequestProcessorMap;
	}

	public void setTxnResponseProcessorMap(Map<String, BizProcessor> txnResponseProcessorMap) {
		this.txnResponseProcessorMap = txnResponseProcessorMap;
	}

	public void setExceptionProcessorMap(Map<String, BizProcessor> exceptionProcessorMap) {
		this.exceptionProcessorMap = exceptionProcessorMap;
	}

	public void setDefaultTxnRequestProcessor(BizProcessor defaultTxnRequestProcessor) {
		this.defaultTxnRequestProcessor = defaultTxnRequestProcessor;
	}

	public void setDefaultTxnResponseProcessor(BizProcessor defaultTxnResponseProcessor) {
		this.defaultTxnResponseProcessor = defaultTxnResponseProcessor;
	}

	public void setDefaultExceptionProcessor(BizProcessor defaultExceptionProcessor) {
		this.defaultExceptionProcessor = defaultExceptionProcessor;
	}
}
