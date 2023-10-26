package com.andyadc.test;

import com.andyadc.workflow.service.OrderProcessService;
import com.andyadc.workflow.service.dto.OrderProcessRequest;
import com.andyadc.workflow.service.dto.OrderProcessResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class OrderIntegrationTests {

	@Autowired
	private OrderProcessService orderProcessService;

	@Test
	public void testOrderProcess() {
		OrderProcessRequest request = new OrderProcessRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setFunctionCode("10");
		request.setPayMode("10");
		OrderProcessResponse response = orderProcessService.process(request);
		System.out.println(response);
	}
}
