package com.andyadc.order.persistence.mapper;

import com.andyadc.order.persistence.entity.Order;

public interface OrderMapper {

	Order selectByPrimaryKey(Long id);
}
