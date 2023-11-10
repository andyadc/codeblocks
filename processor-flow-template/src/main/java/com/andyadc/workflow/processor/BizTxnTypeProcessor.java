package com.andyadc.workflow.processor;

import com.andyadc.workflow.base.BizTypes;
import com.andyadc.workflow.base.TransactionFlow;
import com.andyadc.workflow.base.TxnTypeRequest;
import com.andyadc.workflow.base.Utils;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class BizTxnTypeProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(BizTxnTypeProcessor.class);

	/**
	 * 业务交易类型集合
	 */
	private Map<Map<String, String>, BizTypes> bizTxnTypeMap = new LinkedHashMap<>();

	private static String getRequestKeyValue(String key, TxnTypeRequest request) {
		Object value = Utils.getFieldValue(request, key);
		return String.valueOf(value);
	}

	private static TxnTypeRequest build(TransactionFlow flow) {
		TxnTypeRequest request = new TxnTypeRequest();
		request.setFunctionCode(flow.getFunctionCode());
		request.setChannelType(flow.getChannelType());
		request.setPayMode(flow.getPayMode());
		return request;
	}

	@Override
	public boolean process(ProcessorContext context) throws BizException {
		logger.info("-BizTxnTypeProcessor-");
		TransactionFlow flow = context.getVarValue(ProcessorContext.TRANSACTION_FLOW_REQUEST, TransactionFlow.class);

		TxnTypeRequest request = build(flow);
		BizTypes bizType = getBizType(request);
		if (bizType == null || !StringUtils.hasText(bizType.getCode())) {
			throw new BizException();
		}
		context.setBizType(bizType.getCode());
		return true;
	}

	private BizTypes getBizType(TxnTypeRequest request) {
		for (Map.Entry<Map<String, String>, BizTypes> entry : bizTxnTypeMap.entrySet()) {
			int matchNo = 0;
			for (Map.Entry<String, String> keyEntry : entry.getKey().entrySet()) {
				String key = keyEntry.getKey();
				String value = keyEntry.getValue();

				String requestValue = getRequestKeyValue(key, request);
				if (value.equals(requestValue)) {
					matchNo++;
				}
			}

			if (matchNo == 1) {
				BizTypes value = entry.getValue();
				logger.info("{}", value);
				return value;
			}
		}
		return null;
	}

	public void setBizTxnTypeMap(Map<Map<String, String>, BizTypes> bizTxnTypeMap) {
		this.bizTxnTypeMap = bizTxnTypeMap;
	}
}
