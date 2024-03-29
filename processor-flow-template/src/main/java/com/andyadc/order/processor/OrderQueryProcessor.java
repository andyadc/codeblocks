package com.andyadc.order.processor;

import com.andyadc.workflow.base.ProcessorConstant;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderQueryProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(OrderQueryProcessor.class);

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("--OrderQueryProcessor--");

//		int i = 1 / 0;

		context.setProcessResult(ProcessorConstant.PROCESS_CONTINUE);
	}
}
