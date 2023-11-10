package com.andyadc.workflow.processor;

import com.andyadc.workflow.convertor.ObjectConvertor;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BizObjectProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(BizObjectProcessor.class);

	private ObjectConvertor objectConvertor;

	@Override
	public boolean process(ProcessorContext context) throws BizException {
		logger.info("BizObjectProcessor");

		objectConvertor.convert(context);

		return true;
	}

	public void setObjectConvertor(ObjectConvertor objectConvertor) {
		this.objectConvertor = objectConvertor;
	}
}
