package com.hu.demoSharding1.service;

import com.hu.demoSharding1.DO.Order;

public interface OrderService {
	void addOrder(Order order);
	
	int count(long userId);
}
