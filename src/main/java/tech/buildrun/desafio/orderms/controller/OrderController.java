package tech.buildrun.desafio.orderms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.desafio.orderms.controller.dto.ApiResponse;
import tech.buildrun.desafio.orderms.controller.dto.OrderResponse;
import tech.buildrun.desafio.orderms.controller.dto.PaginationResponse;
import tech.buildrun.desafio.orderms.service.OrderService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private OrderService orderService;

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOrders", totalOnOrders),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
    }
}
