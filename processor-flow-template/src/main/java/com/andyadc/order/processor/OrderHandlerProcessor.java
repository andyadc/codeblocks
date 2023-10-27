package com.andyadc.order.processor;

import com.andyadc.workflow.base.ProcessorConstant;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderHandlerProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(OrderHandlerProcessor.class);

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("--OrderHandlerProcessor--");

		context.setProcessResult(ProcessorConstant.PROCESS_CONTINUE);
	}
}
