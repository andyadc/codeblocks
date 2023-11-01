package com.andyadc.workflow.service;

import com.andyadc.workflow.service.dto.OrderProcessRequest;
import com.andyadc.workflow.service.dto.OrderProcessResponse;

public interface OrderProcessService {

	OrderProcessResponse process(OrderProcessRequest request);
}
