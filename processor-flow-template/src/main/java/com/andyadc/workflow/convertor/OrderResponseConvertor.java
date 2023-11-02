package com.andyadc.workflow.convertor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.service.dto.OrderProcessRequest;
import com.andyadc.workflow.service.dto.OrderProcessResponse;

public class OrderResponseConvertor implements ObjectConvertor {

	@Override
	public void convert(ProcessorContext context) throws BizException {
		OrderProcessResponse processResponse = context.getVarValue(ProcessorContext.ORDER_RESPONSE, OrderProcessResponse.class);
		if (processResponse != null) {
			return;
		}
		OrderProcessRequest request = context.getVarValue(ProcessorContext.ORDER_REQUEST, OrderProcessRequest.class);

		processResponse = new OrderProcessResponse();
		processResponse.setRequestId(request.getRequestId());


		context.setVariables(ProcessorContext.ORDER_RESPONSE, processResponse);
	}
}
