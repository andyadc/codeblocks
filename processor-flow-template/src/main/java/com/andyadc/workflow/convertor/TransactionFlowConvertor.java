package com.andyadc.workflow.convertor;

import com.andyadc.workflow.base.TransactionFlow;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.service.dto.OrderProcessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionFlowConvertor implements ObjectConvertor {

	private static final Logger logger = LoggerFactory.getLogger(TransactionFlowConvertor.class);

	@Override
	public void convert(ProcessorContext context) throws BizException {
		logger.info("---");
		OrderProcessRequest request = context.getVarValue(ProcessorContext.ORDER_REQUEST, OrderProcessRequest.class);

		TransactionFlow flow = new TransactionFlow();
		flow.setFunctionCode(request.getFunctionCode());
		flow.setPayMode(request.getPayMode());

		context.setVariables(ProcessorContext.TRANSACTION_FLOW_REQUEST, flow);
	}
}
