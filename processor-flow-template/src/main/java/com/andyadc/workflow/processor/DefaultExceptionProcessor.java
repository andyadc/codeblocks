package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExceptionProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionProcessor.class);

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("-DefaultExceptionProcessor-");
	}
}
