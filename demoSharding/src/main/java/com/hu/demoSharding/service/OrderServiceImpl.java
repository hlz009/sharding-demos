package com.hu.demoSharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hu.demoSharding.DO.Order;
import com.hu.demoSharding.dao.OrderDao;

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
