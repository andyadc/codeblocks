package com.andyadc.workflow.processor;

import com.andyadc.workflow.base.RouterRequest;
import com.andyadc.workflow.base.RouterResult;
import com.andyadc.workflow.base.TransactionFlow;
import com.andyadc.workflow.base.Utils;
import com.andyadc.workflow.exception.BizException;
import com.andyadc.workflow.processor.core.BizProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class RouterProcessor implements BizProcessor {

	private static final Logger logger = LoggerFactory.getLogger(RouterProcessor.class);

	private LinkedHashMap<Map<String, String>, String> routerRules = new LinkedHashMap<>();

	private static String getRequestKeyValue(String key, RouterRequest request) {
		Object value = Utils.getFieldValue(request, key);
		return String.valueOf(value);
	}

	@Override
	public void process(ProcessorContext context) throws BizException {
		logger.info("-RouterProcessor-");
		if (StringUtils.hasText(context.getIntegrationChannelType())) {
			logger.info("IntegrationChannelType [{}] existed", context.getIntegrationChannelType());
			return;
		}
		RouterRequest request = build(context);
		RouterResult result = calcRouterRules(request);
		if (!StringUtils.hasText(result.getIntegrationChannelType())) {
			logger.warn("IntegrationChannelType is null");
			throw new BizException();
		}
		context.setIntegrationChannelType(result.getIntegrationChannelType());
	}

	public RouterRequest build(ProcessorContext context) {
		RouterRequest request = new RouterRequest();
		TransactionFlow flow = context.getVarValue(ProcessorContext.TRANSACTION_FLOW_REQUEST, TransactionFlow.class);
		request.setFunctionCode(flow.getFunctionCode());
		request.setPayMode(flow.getPayMode());
		return request;
	}

	private RouterResult calcRouterRules(RouterRequest request) {
		RouterResult result = new RouterResult();
		int size = request.conditionSize();
		for (Map.Entry<Map<String, String>, String> rule : routerRules.entrySet()) {
			int matchNo = 0;
			for (Map.Entry<String, String> entry : rule.getKey().entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				String requestValue = getRequestKeyValue(key, request);
				if (value.equals(requestValue)) {
					matchNo++;
				}
			}

			if (size == matchNo) {
				String type = rule.getValue();
				logger.info("found type: {}, {}", type, request);
				result.setIntegrationChannelType(type);
			}
		}
		return result;
	}

	public void setRouterRules(LinkedHashMap<Map<String, String>, String> routerRules) {
		this.routerRules = routerRules;
	}
}
