package tech.buildrun.desafio.oderms.repository;

import org.springframework.data.querydsl.QPageRequest;
import tech.buildrun.desafio.oderms.controller.OrderResponse;
import tech.buildrun.desafio.oderms.entity.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    page<OrderEntity> findAllByCustomerId(long customerId, pageRequest pageRequest);
}
