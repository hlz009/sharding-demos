package com.hu.demoSharding.service;

import com.hu.demoSharding.DO.Order;

public interface OrderService {
	void addOrder(Order order);
	
	int count(long userId);
}
