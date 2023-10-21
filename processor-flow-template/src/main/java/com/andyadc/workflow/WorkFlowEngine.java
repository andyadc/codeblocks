package com.andyadc.workflow;

import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.BizProcessorProxy;
import com.andyadc.workflow.processor.ProcessorContext;
import com.andyadc.workflow.processor.TxnProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WorkFlowEngine {

	private static final Logger logger = LoggerFactory.getLogger(WorkFlowEngine.class);

	private TxnProcessorFactory txnProcessorFactory;

	private Map<String, BizWorkFlowMap> workFlowMap = new HashMap<>();

	public void processWorkFlow(ProcessorContext context) {
		//下个工作流的标示
		String nextWorkValue = null;
		//下个工作流处理器
		BizProcessorProxy nextProcessor = null;
		try {
			// 获取渠道类型的工作流集合对象
			BizWorkFlowMap workFlowMap = getWorkFlow(context);
			Map<String, BizProcessorProxy> processorProxyMap = workFlowMap.getWorkFlowMap();

			BizProcessorProxy currentProcessor = processorProxyMap.get(context.getCurrentWork());
			if (context.getProcessResult() != null) {
				nextWorkValue = currentProcessor.getNextWorkMap().get(context.getProcessResult());
				nextProcessor = workFlowMap.get(nextWorkValue);
			}
			if (nextProcessor == null) {
				return;
			}

			context.setProcessResult(null);
			context.setCurrentWork(nextWorkValue);
			nextProcessor.process(context);
		} catch (BizException e) {
			logger.error("processWorkFlow error", e);
			exceptionProcess(context, e.getCode());
		} catch (Exception e) {
			logger.error("processWorkFlow error", e);
		}
		processWorkFlow(context);
	}

	private void exceptionProcess(ProcessorContext context, String code) {

	}

	private BizWorkFlowMap getWorkFlow(ProcessorContext context) {
		BizWorkFlowMap bizWorkFlowMap = workFlowMap.get("");
		if (bizWorkFlowMap == null) {
			throw new BizException();
		}
		return bizWorkFlowMap;
	}

	public void setTxnProcessorFactory(TxnProcessorFactory txnProcessorFactory) {
		this.txnProcessorFactory = txnProcessorFactory;
	}

	public void setWorkFlowMap(Map<String, BizWorkFlowMap> workFlowMap) {
		this.workFlowMap = workFlowMap;
	}
}
