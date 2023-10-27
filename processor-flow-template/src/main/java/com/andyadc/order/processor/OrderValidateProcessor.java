package com.andyadc.order.processor;

import com.andyadc.workflow.base.ProcessorConstant;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderValidateProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(OrderValidateProcessor.class);

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("validate");

		context.setProcessResult(ProcessorConstant.PROCESS_CONTINUE);
	}
}
