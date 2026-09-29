package com.snackvending.repository;

import com.snackvending.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, String> {
    List<Orders> findAllByOrderByCreateTimeDesc();
}
