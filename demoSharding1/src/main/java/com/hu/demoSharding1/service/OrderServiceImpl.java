package com.hu.demoSharding1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hu.demoSharding1.DO.Order;
import com.hu.demoSharding1.dao.OrderDao;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDao orderDao;

	@Override
	public void addOrder(Order order) {
		orderDao.save(order);
	}

	@Override
	public int count(long userId) {
		return orderDao.countByUserId(userId);
	}
}
