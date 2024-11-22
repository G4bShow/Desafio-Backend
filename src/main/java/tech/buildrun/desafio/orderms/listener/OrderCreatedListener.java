package tech.buildrun.desafio.orderms.listener;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import tech.buildrun.desafio.orderms.listener.dto.OrderCreatedEvent;
import tech.buildrun.desafio.orderms.service.OrderService;

import static tech.buildrun.desafio.orderms.Config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
@AllArgsConstructor
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
}