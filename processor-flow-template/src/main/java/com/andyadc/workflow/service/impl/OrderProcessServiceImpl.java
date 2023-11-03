package com.andyadc.workflow.service.impl;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.ProcessorContextHelper;
import com.andyadc.workflow.processor.TxnProcessorFactory;
import com.andyadc.workflow.processor.core.Processor;
import com.andyadc.workflow.service.OrderProcessService;
import com.andyadc.workflow.service.dto.OrderProcessRequest;
import com.andyadc.workflow.service.dto.OrderProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderProcessServiceImpl implements OrderProcessService {

	private static final Logger logger = LoggerFactory.getLogger(OrderProcessServiceImpl.class);

	private Processor processorChain;

	private TxnProcessorFactory txnProcessorFactory;

	@Override
	public OrderProcessResponse process(OrderProcessRequest request) {
		ProcessorContext context = buildProcessorContext(request);
		try {
			processorChain.process(context);
		} catch (BizException e) {
			logger.error("biz error", e);
			processException(context, e.getCode());
		} catch (Exception e) {
			logger.error("sys error", e);
			processException(context, "-1");
		}
		OrderProcessResponse processResponse = context.getVarValue(ProcessorContext.ORDER_RESPONSE, OrderProcessResponse.class);
		ProcessorContextHelper.remove();
		return processResponse;
	}

	private void processException(ProcessorContext context, String errorCode) {
		txnProcessorFactory.getExceptionProcessor(context).process(context);
	}

	private ProcessorContext buildProcessorContext(OrderProcessRequest request) {
		ProcessorContext context = new ProcessorContext();
		context.setVariables(ProcessorContext.ORDER_REQUEST, request);
		ProcessorContextHelper.addProcessorContext(context);
		return context;
	}

	private void releaseProcessorContext() {
		ProcessorContextHelper.remove();
	}

	public void setProcessorChain(Processor processorChain) {
		this.processorChain = processorChain;
	}

	public void setTxnProcessorFactory(TxnProcessorFactory txnProcessorFactory) {
		this.txnProcessorFactory = txnProcessorFactory;
	}
}
