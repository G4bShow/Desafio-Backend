package tech.buildrun.desafio.oderms.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.desafio.oderms.service.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,){

        var pageResponse = OrderService.findAllByCustomerId(CustomerId, PageRequest.of(page, pageSize));
        var totalOnOrders = OrderService.findTotalOnOrdersByCustomerId(CustomerId);


        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOrders", totalOnOrders),
                PageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
                ));

    }
}
