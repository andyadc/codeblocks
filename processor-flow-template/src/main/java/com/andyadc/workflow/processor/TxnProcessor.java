package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.BizProcessor;
import com.andyadc.workflow.processor.core.Processor;

public class TxnProcessor implements Processor {

	private TxnProcessorFactory txnProcessorFactory;

	@Override
	public boolean process(ProcessorContext context) throws BizException {
		BizProcessor processor = txnProcessorFactory.getTxnRequestProcessor(context);
		processor.process(context);
		return true;
	}

	public void setTxnProcessorFactory(TxnProcessorFactory txnProcessorFactory) {
		this.txnProcessorFactory = txnProcessorFactory;
	}
}
