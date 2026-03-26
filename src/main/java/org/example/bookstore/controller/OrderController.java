package org.example.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.order.PlaceSingleBookDTO;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.Long;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PutMapping("/place")
    public ResponseEntity<ServerResponseDto> placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(orderService.placeOrder(placeOrderDTO, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ServerResponseDto> getOrderByUserId(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) String sortBy,
                                                          @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId, page, size, sortBy, sortDirection));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> getOrderByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ServerResponseDto.success(orderService.getOrder(orderId)));
    }

    @GetMapping("/all")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) String sortDirection) {
        List<OrderDTO> orderDTOList = orderService.getAllOrders(page, size, sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK).body(ServerResponseDto.success(orderDTOList));
    }

    @PostMapping("/update")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> updateOrder(@RequestParam Long orderId,
                                                     @RequestParam int orderStatus) {
        return ResponseEntity.ok(orderService.updateStatusOrder(orderId, orderStatus));

    }
    @PostMapping("/cancel")
    public ResponseEntity<ServerResponseDto> cancelOrder(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PostMapping("/confirm/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> confirmOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PostMapping("/transit/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> transitOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.transitOrder(orderId));
    }

    @PostMapping("/delivery/{id}")
    @PreAuthorize("@authorizationService.isAdmin()")
    public ResponseEntity<ServerResponseDto> deliveryOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.deliveryOrder(orderId));
    }

    @GetMapping("/number")
    public ResponseEntity<ServerResponseDto> getNumberOfOrders() {
        return ResponseEntity.ok(ServerResponseDto.success(orderRepository.countOrder()));
    }

    @PostMapping("/buyNow")
    public ResponseEntity<ServerResponseDto> buyNow(@RequestBody PlaceSingleBookDTO placeOrder, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(orderService.buyNow(placeOrder, request));
    }

}
