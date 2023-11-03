package com.andyadc.workflow.processor.core;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;

public interface BizProcessor {

	void process(ProcessorContext context) throws BizException;
}
