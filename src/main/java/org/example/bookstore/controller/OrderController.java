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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PutMapping("/placeOrder")
    public ResponseEntity<ServerResponseDto> placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(orderService.placeOrder(placeOrderDTO, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ServerResponseDto> getOrderByUserId(@PathVariable UUID userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(required = false) String sortBy,
                                                              @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId, page, size, sortBy, sortDirection));
    }

    @GetMapping("getOrderByOrderId/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ServerResponseDto> getOrderByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(ServerResponseDto.success(orderService.getOrder(orderId)));
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) String sortDirection) {
        List<OrderDTO> orderDTOList = orderService.getAllOrders(page, size, sortBy, sortDirection);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(orderDTOList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PostMapping("/updateOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> updateOrder(@RequestParam UUID orderId,
                                                         @RequestParam int orderStatus) {
        return ResponseEntity.ok(orderService.updateStatusOrder(orderId, orderStatus));

    }
    @PostMapping("/cancelOrder")
    public ResponseEntity<ServerResponseDto> cancelOrder(@RequestParam UUID orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PostMapping("/confirmOrder/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> confirmOrder(@PathVariable("id") UUID orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PostMapping("/transitOrder/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> transitOrder(@PathVariable("id") UUID orderId) {
        return ResponseEntity.ok(orderService.transitOrder(orderId));
    }

    @PostMapping("/deliveryOrder/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServerResponseDto> deliveryOrder(@PathVariable("id") UUID orderId) {
        return ResponseEntity.ok(orderService.deliveryOrder(orderId));
    }

    @GetMapping("/getNumberOfOrders")
    public ResponseEntity<ServerResponseDto> getNumberOfOrders() {
        return ResponseEntity.ok(ServerResponseDto.success(orderRepository.countOrder()));
    }

    @PostMapping("/buyNow")
    public ResponseEntity<ServerResponseDto> buyNow(@RequestBody PlaceSingleBookDTO placeOrder, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(orderService.buyNow(placeOrder, request));
    }

}
