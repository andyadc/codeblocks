package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;

public interface Processor {

	boolean process(ProcessorContext context) throws BizException;
}
