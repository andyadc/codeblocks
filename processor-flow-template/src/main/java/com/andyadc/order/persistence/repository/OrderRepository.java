package com.andyadc.order.persistence.repository;

import com.andyadc.order.persistence.entity.Order;
import com.andyadc.order.persistence.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

	private OrderMapper orderMapper;

	public Order get(Long id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
}
