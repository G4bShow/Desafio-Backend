package tech.buildrun.desafio.oderms.listener.Order.Creater.dto;

import java.math.BigDecimal;

public record OrderItemEvent(String produto,
                             Integer quantidade,
                             BigDecimal price) {
}
