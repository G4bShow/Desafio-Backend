package tech.buildrun.desafio.orderms.service;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import tech.buildrun.desafio.orderms.controller.dto.OrderResponse;
import tech.buildrun.desafio.orderms.entity.OrderEntity;
import tech.buildrun.desafio.orderms.entity.OrderItem;
import tech.buildrun.desafio.orderms.listener.dto.OrderCreatedEvent;
import tech.buildrun.desafio.orderms.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public void save(OrderCreatedEvent event) {
        var entity = OrderEntity.builder()
                .orderId(event.codigoPedido())
                .customerId(event.codigoCliente())
                .items(getOrderItems(event))
                .total(getTotal(event))
                .build();

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());

    }

    /*OBS1*/
    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /*OBS2*/
    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> OrderItem.builder()
                        .product(i.produto())
                        .quantity(i.quantidade())
                        .price(i.preco())
                        .build())
                .toList();
    }
}
