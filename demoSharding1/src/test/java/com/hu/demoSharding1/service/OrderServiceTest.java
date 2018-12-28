package com.hu.demoSharding1.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hu.demoSharding1.DO.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Test
	public void test() {
		Order order = new Order();
		order.setUserId(201800111L);
		order.setOrderNo("TB2018040201");
		try {
			orderService.addOrder(order);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFind() {
		try {
			int i = orderService.count(2018040200);
			System.out.println("查询的结果为：" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
