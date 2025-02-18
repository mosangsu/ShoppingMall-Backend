package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByMemberMemberId(Long memberId);
    Order findByOrderNumber(String orderNumber);
}