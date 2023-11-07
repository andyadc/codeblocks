package com.andyadc.workflow.processor.core;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;

import java.util.List;

public class ProcessorChain implements Processor {

	/**
	 * 业务处理器集合
	 */
	private List<Processor> processorList;

	@Override
	public boolean process(ProcessorContext context) throws BizException {
		boolean isContinue = true;
		if (processorList != null && processorList.size() > 0) {
			for (Processor processor : processorList) {
				isContinue = processor.process(context);
				if (!isContinue) {
					break;
				}
			}
		}
		return isContinue;
	}

	public void setProcessorList(List<Processor> processorList) {
		this.processorList = processorList;
	}
}
