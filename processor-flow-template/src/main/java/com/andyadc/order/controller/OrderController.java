package com.andyadc.order.controller;

import com.andyadc.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
public class OrderController {

	private OrderService orderService;

	@RequestMapping("/test")
	public Object test() {
		return orderService.get(1L);
	}

	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
