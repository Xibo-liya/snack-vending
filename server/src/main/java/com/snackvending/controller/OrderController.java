package com.snackvending.controller;

import com.snackvending.dto.ApiResponse;
import com.snackvending.entity.Orders;
import com.snackvending.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            Double totalAmount = ((Number) body.get("totalAmount")).doubleValue();
            Integer totalCount = ((Number) body.get("totalCount")).intValue();

            Orders order = orderService.createOrder(items, totalAmount, totalCount);
            Map<String, Object> result = orderToMap(order);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("订单创建失败：" + e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/pay")
    public ApiResponse<Map<String, Object>> pay(@PathVariable String orderNo) {
        try {
            Orders order = orderService.pay(orderNo);
            return ApiResponse.success(orderToMap(order));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/cancel")
    public ApiResponse<Map<String, Object>> cancel(@PathVariable String orderNo) {
        try {
            Orders order = orderService.cancel(orderNo);
            return ApiResponse.success(orderToMap(order));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        List<Orders> orders = orderService.list();
        List<Map<String, Object>> result = orders.stream().map(this::orderToMap).toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable String orderNo) {
        Orders order = orderService.getDetail(orderNo);
        if (order == null) {
            return ApiResponse.error("订单不存在");
        }
        return ApiResponse.success(orderToMap(order));
    }

    private Map<String, Object> orderToMap(Orders order) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", order.getOrderNo());
        map.put("totalAmount", order.getTotalAmount());
        map.put("totalCount", order.getTotalCount());
        map.put("status", order.getStatus());
        map.put("createTime", order.getCreateTime() == null ? null : order.getCreateTime().toString().replace("T", " "));
        map.put("payTime", order.getPayTime() == null ? null : order.getPayTime().toString().replace("T", " "));
        map.put("items", orderService.parseItems(order));
        return map;
    }
}
