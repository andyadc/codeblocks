package com.andyadc.order.service;

import com.andyadc.order.persistence.entity.Order;
import com.andyadc.order.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	public Order get(Long id) {
		return orderRepository.get(id);
	}

	@Autowired
	public void setOrderRepository(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
}
