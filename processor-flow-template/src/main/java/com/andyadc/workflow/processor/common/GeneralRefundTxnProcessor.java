package com.andyadc.workflow.processor.common;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.core.BizProcessor;
import com.andyadc.workflow.processor.core.WorkFlowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralRefundTxnProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(GeneralRefundTxnProcessor.class);

	private WorkFlowEngine workFlowEngine;
	private BizProcessor routerProcessor;

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("-GeneralRefundTxnProcessor-");

		routerProcessor.process(context);

		workFlowEngine.processWorkFlow(context);
	}

	public void setWorkFlowEngine(WorkFlowEngine workFlowEngine) {
		this.workFlowEngine = workFlowEngine;
	}

	public void setRouterProcessor(BizProcessor routerProcessor) {
		this.routerProcessor = routerProcessor;
	}
}
