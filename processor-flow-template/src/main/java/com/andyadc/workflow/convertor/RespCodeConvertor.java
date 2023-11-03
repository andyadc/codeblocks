package com.andyadc.workflow.convertor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RespCodeConvertor implements ObjectConvertor {

	private static final Logger logger = LoggerFactory.getLogger(RespCodeConvertor.class);

	@Override
	public void convert(ProcessorContext context) throws BizException {
		logger.info("-RespCodeConvertor-");
	}
}
