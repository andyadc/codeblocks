package com.andyadc.order.processor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.BizProcessor;
import com.andyadc.workflow.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderValidateProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(OrderValidateProcessor.class);

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("validate");
	}
}
