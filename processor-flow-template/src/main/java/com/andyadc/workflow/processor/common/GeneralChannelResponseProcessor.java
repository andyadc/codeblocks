package com.andyadc.workflow.processor.common;

import com.andyadc.workflow.convertor.ObjectConvertor;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.TxnProcessorFactory;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralChannelResponseProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(GeneralChannelResponseProcessor.class);

	private TxnProcessorFactory txnProcessorFactory;
	private ObjectConvertor objectConvertor;

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("GeneralChannelResponseProcessor");

		genTxnResponse(context);

		responseProcessor(context);
	}

	private void responseProcessor(ProcessorContext context) {
		txnProcessorFactory.getTxnResponseProcessor(context).process(context);
	}

	protected void genTxnResponse(ProcessorContext context) {
		objectConvertor.convert(context);
	}

	public void setTxnProcessorFactory(TxnProcessorFactory txnProcessorFactory) {
		this.txnProcessorFactory = txnProcessorFactory;
	}

	public void setObjectConvertor(ObjectConvertor objectConvertor) {
		this.objectConvertor = objectConvertor;
	}
}
