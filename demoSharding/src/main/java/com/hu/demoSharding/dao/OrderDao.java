package com.hu.demoSharding.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.hu.demoSharding.DO.Order;

public interface OrderDao extends PagingAndSortingRepository<Order, Long> {
 
	Order findByUserId(Long userId);

	@Query(value = "SELECT COUNT(*) FROM t_order WHERE user_id = :userId ", nativeQuery = true)
	int countByUserId(@Param("userId") Long userId);
//	@Query("from order where user_id = :id")
//	List<Order> queryList(@Param("id") Long id, Pageable pageable);
}
