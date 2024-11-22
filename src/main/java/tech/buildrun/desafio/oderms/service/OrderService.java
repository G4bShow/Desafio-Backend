package tech.buildrun.desafio.oderms.service;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import tech.buildrun.desafio.oderms.controller.OrderResponse;
import tech.buildrun.desafio.oderms.entity.OrderEntity;
import tech.buildrun.desafio.oderms.entity.OrderItem;
import tech.buildrun.desafio.oderms.listener.Order.Creater.dto.OrderCreateEvent;
import tech.buildrun.desafio.oderms.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = new MongoTemplate(mongoTemplate);
    }

    public void save(OrderCreateEvent event) {
        var entity = new OrderEntity();

        entity.setOrderId(event.codigoPedido());
        entity.setOrderId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);

    }

    public Page<OrderResponse> findAllBycustomerId(Long customerId, PageRequest pageRequest) {
        var orders =  orderRepository.findAllByCustomerId(customerId, PageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                math(Criteria.where(key:"customerId").is(customerId)),
                group().sum(reference:"total").as(alias:"total")
        );

        var response = MongoTemplate.Aggregate(aggregations, collectioName:"tb_orders", Document.class)

        return new BigDecimal(response.getUniqueMappedReasult().get("total").toString());

    }
    /*OBS1*/
    private BigDecimal getTotal(OrderCreateEvent event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /*OBS2*/
    private static List<OrderItem> getOrderItems(OrderCreateEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
