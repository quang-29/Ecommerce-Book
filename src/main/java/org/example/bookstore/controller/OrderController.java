package org.example.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.payload.response.PlaceOrderResponse;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @PutMapping("/placeOrder")
    public ResponseEntity<DataResponse> placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO, HttpServletRequest request) throws Exception {

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(placeOrderDTO, request);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(placeOrderResponse)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("getOrderByUserId/{userId}")
    public ResponseEntity<DataResponse> getOrderByUserId(@PathVariable UUID userId) {
        List<OrderDTO> orderDTOList = orderService.getOrdersByUserId(userId);
        DataResponse dataResponse = DataResponse.builder()
                .data(orderDTOList)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
    @GetMapping("getOrderByOrderId/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getOrderByOrderId(@PathVariable UUID orderId) {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        DataResponse dataResponse = DataResponse.builder()
                .data(orderDTO)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getAllOrders() {
        List<OrderDTO> orderDTOList = orderService.getAllOrders();
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
    public ResponseEntity<DataResponse> updateOrder(@RequestParam UUID orderId,
                                                    @RequestParam int orderStatus) {
        OrderDTO orderDTO = orderService.updateOrder(orderId, orderStatus);
        DataResponse dataResponse = DataResponse.builder()
                .data(orderDTO)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);

    }
    @PostMapping("/cancelOrder")
    public ResponseEntity<DataResponse> cancelOrder(@RequestParam UUID orderId) {
        String result = orderService.cancelOrder(orderId);
        DataResponse dataResponse = DataResponse.builder()
                .data(result)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PostMapping("/confirmOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> confirmOrder(@RequestParam UUID orderId) {
        String result = orderService.confirmOrder(orderId);
        DataResponse dataResponse = DataResponse.builder()
                .data(result)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/getNumberOfOrders")
    public ResponseEntity<?> getNumberOfOrders() {
        int number = orderRepository.countOrder();
        return ResponseEntity.status(HttpStatus.OK).body(number);
    }

}
