package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;

public interface BizProcessor {

	void process(ProcessorContext context) throws BizException;
}
