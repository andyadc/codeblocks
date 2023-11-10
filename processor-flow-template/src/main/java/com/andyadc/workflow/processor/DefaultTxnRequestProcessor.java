package com.andyadc.workflow.processor;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.BizProcessor;
import com.andyadc.workflow.processor.core.WorkFlowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DefaultTxnRequestProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTxnRequestProcessor.class);

	private WorkFlowEngine workFlowEngine;
	private BizProcessor routerProcessor;

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("-DefaultTxnRequestProcessor-");
		context.setContextId(UUID.randomUUID().toString());

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
