package com.andyadc.workflow.convertor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;

public interface ObjectConvertor {

	void convert(ProcessorContext context) throws BizException;
}
