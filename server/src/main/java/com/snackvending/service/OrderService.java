package com.snackvending.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snackvending.entity.OrderItem;
import com.snackvending.entity.Orders;
import com.snackvending.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepo, ObjectMapper objectMapper) {
        this.orderRepo = orderRepo;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Orders createOrder(List<Map<String, Object>> items, Double totalAmount, Integer totalCount) {
        Orders order = new Orders();
        order.setOrderNo("SN" + System.currentTimeMillis());
        order.setTotalAmount(totalAmount);
        order.setTotalCount(totalCount);
        order.setStatus("pending");

        for (Map<String, Object> item : items) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductId(((Number) item.get("id")).longValue());
            oi.setProductName((String) item.get("name"));
            oi.setPrice(((Number) item.get("price")).doubleValue());
            oi.setImage((String) item.get("image"));
            oi.setQuantity((Integer) item.get("qty"));
            order.getItems().add(oi);
        }

        try {
            order.setItemsJson(objectMapper.writeValueAsString(items));
        } catch (Exception e) {
            order.setItemsJson("[]");
        }

        return orderRepo.save(order);
    }

    @Transactional
    public Orders pay(String orderNo) {
        Orders order = orderRepo.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("订单状态异常，无法支付");
        }
        order.setStatus("paid");
        order.setPayTime(LocalDateTime.now());
        return orderRepo.save(order);
    }

    @Transactional
    public Orders cancel(String orderNo) {
        Orders order = orderRepo.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus("timeout");
        return orderRepo.save(order);
    }

    public Orders getDetail(String orderNo) {
        return orderRepo.findById(orderNo).orElse(null);
    }

    public List<Orders> list() {
        return orderRepo.findAllByOrderByCreateTimeDesc();
    }

    /** 将订单 itemsJson 反序列化为前端需要的结构 */
    public List<Map<String, Object>> parseItems(Orders order) {
        try {
            return objectMapper.readValue(order.getItemsJson(), new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
