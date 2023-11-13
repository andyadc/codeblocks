package com.andyadc.workflow.processor;

import com.andyadc.workflow.convertor.ObjectConvertor;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTxnResponseProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTxnResponseProcessor.class);

	private ObjectConvertor objectConvertor;

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("-DefaultTxnResponseProcessor-");

		objectConvertor.convert(context);

		releaseContext(context);
	}

	private void releaseContext(ProcessorContext context) {
		ProcessorContextHelper.remove();
	}

	public void setObjectConvertor(ObjectConvertor objectConvertor) {
		this.objectConvertor = objectConvertor;
	}
}
