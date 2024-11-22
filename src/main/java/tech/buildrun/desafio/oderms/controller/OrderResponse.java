package tech.buildrun.desafio.oderms.controller;

import tech.buildrun.desafio.oderms.entity.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {
    public static OrderResponse from(OrderEntity entity){
         return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
