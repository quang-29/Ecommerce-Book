package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.*;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.*;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.OrderItemDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.order.PlaceSingleBookDTO;
import org.example.bookstore.payload.response.PlaceOrderResponse;
import org.example.bookstore.repository.*;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.service.Interface.NotificationService;
import org.example.bookstore.service.Interface.UserAddressService;
import org.example.bookstore.service.shipment.GHNService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
    private final GHNService ghnService;
    private final VNPayService vnPayService;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final UserAddressService userAddressService;





    public OrderService(CartRepository cartRepository, UserRepository userRepository, ModelMapper modelMapper, PaymentRepository paymentRepository, OrderRepository orderRepository, CartService cartService, OrderItemRepository orderItemRepository, BookRepository bookRepository, GHNService ghnService, VNPayService vnPayService, NotificationRepository notificationRepository, NotificationService notificationService, UserAddressService userAddressService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
        this.ghnService = ghnService;
        this.vnPayService = vnPayService;
        this.userAddressService = userAddressService;
    }


    @Transactional
    public ServerResponseDto placeOrder(PlaceOrderDTO placeOrderDTO, HttpServletRequest httpServletRequest) throws Exception {

        Cart cart = cartRepository.findById(placeOrderDTO.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_ERROR);
        }

        long allBookPrice = cartItems.stream()
                .mapToLong(item -> item.getBookPrice() * item.getQuantity())
                .sum();

        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        if (placeOrderDTO.getAddressId() == null) {
            throw new AppException(ErrorCode.INVALID_ADDRESS);
        }
        UserAddress addressTo = userAddressList.stream()
                .filter(address -> address.getId().equals(placeOrderDTO.getAddressId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        ShipmentInfo shipmentInfo = ShipmentInfo.builder()
                .from(new ShopAddress())
                .to(addressTo)
                .weight(placeOrderDTO.getWeight())
                .build();

        BasicShippingOrderInfo basicShippingOrderInfo = ghnService.calculateShipmentFee(shipmentInfo);
        long shippingFee = basicShippingOrderInfo.getFee();

        long totalPay = allBookPrice + shippingFee;

        PaymentType paymentType = PaymentType.fromString(placeOrderDTO.getPaymentType());

        if(paymentType == null) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND);
        }

        Payment payment = new Payment();
        payment.setType(paymentType);
        payment.setCreatedAt(new Date());
        payment.setFeeShip(shippingFee);
        payment.setAmount(totalPay);
        if(payment.getType() != PaymentType.COD) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(payment.getCreatedAt());
            calendar.add(Calendar.MINUTE, 2);
            payment.setExpireAt(calendar.getTime());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setGateway(PaymentGateway.VNPAY);
        }
        else {
            payment.setStatus(PaymentStatus.COD);
            payment.setGateway(PaymentGateway.COD);
        }


        Order order = new Order();
        order.setCreateAt(new Date());
        order.setUser(user);
        order.setUserAddress(addressTo);
        order.setPayment(payment);
        order.setEstimatedDeliveryDate(basicShippingOrderInfo.getExpectedDeliveryDate());
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(cartItem.getBookPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        for (int i = 0; i < cart.getCartItems().size(); i++) {
            CartItem cartItem1 = cart.getCartItems().get(i);
            int quantity = cartItem1.getQuantity();
            Book book = cartItem1.getBook();
            cartService.deleteProductFromCart(cart.getId(), book.getId());
            book.setStock(book.getStock() - quantity);
            book.setSold(book.getSold() + quantity);
            bookRepository.save(book);
        }

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(order.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(order, httpServletRequest);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);
    }

    public ServerResponseDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setOrderItem(order.getOrderItems().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class)).collect(Collectors.toList()));
        return ServerResponseDto.success(orderDTO);
    }

    public ServerResponseDto getOrdersByUserId(UUID userId, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orderDTOPage = orderRepository.findAllOrderByUserId(userId, pageable).map(order -> modelMapper.map(order, OrderDTO.class));
        return ServerResponseDto.success(orderDTOPage);
    }


    public List<OrderDTO> getAllOrders(int page, int size, String sortBy, String sortDirection) {
        List<Order> orders = orderRepository.findAll();
        if (orders.size() == 0) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
                    orderDTO.setOrderItem(order.getOrderItems().stream()
                            .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                            .collect(Collectors.toList()));
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ServerResponseDto updateStatusOrder(UUID orderId, int orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setOrderStatus(orderStatus);
        return ServerResponseDto.success(modelMapper.map(orderRepository.save(order), OrderDTO.class));
    }

    public ServerResponseDto cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.CANCELLED);
        order.setPayment(payment);
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);
        for (OrderItem orderItem : orderItems) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());
             bookRepository.save(book);
        }
        return ServerResponseDto.success("Order has been canceled successfully");
    }

    public ServerResponseDto confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.CONFIRMED);
        order.setPayment(payment);
        orderRepository.save(order);
        return ServerResponseDto.success("Confirm order successfully");
    }

    public ServerResponseDto transitOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.IN_TRANSIT);
        order.setPayment(payment);
        orderRepository.save(order);
        return ServerResponseDto.success("Start delivery order");
    }

    public ServerResponseDto deliveryOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.DELIVERED);
        order.setPayment(payment);
        orderRepository.save(order);
        return ServerResponseDto.success("Delivery order successfully");
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    public ServerResponseDto buyNow(PlaceSingleBookDTO placeSingleBookDTO, HttpServletRequest request) throws Exception {
        Book book = bookRepository.findById(placeSingleBookDTO.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        if (placeSingleBookDTO.getAddressId() == null) {
            throw new AppException(ErrorCode.INVALID_ADDRESS);
        }
        UserAddress addressTo = userAddressList.stream()
                .filter(address -> address.getId().equals(placeSingleBookDTO.getAddressId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        ShipmentInfo shipmentInfo = ShipmentInfo.builder()
                .from(new ShopAddress())
                .to(addressTo)
                .weight(placeSingleBookDTO.getWeight())
                .build();

        BasicShippingOrderInfo basicShippingOrderInfo = ghnService.calculateShipmentFee(shipmentInfo);
        long shippingFee = basicShippingOrderInfo.getFee();

        long totalPay = book.getPrice() + shippingFee;

        PaymentType paymentType = PaymentType.fromString(placeSingleBookDTO.getPaymentType());

        if(paymentType == null) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND);
        }

        Payment payment = new Payment();
        payment.setType(paymentType);
        payment.setCreatedAt(new Date());
        payment.setFeeShip(shippingFee);
        payment.setAmount(totalPay);
        if(payment.getType() != PaymentType.COD) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(payment.getCreatedAt());
            calendar.add(Calendar.MINUTE, 2);
            payment.setExpireAt(calendar.getTime());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setGateway(PaymentGateway.VNPAY);
        }
        else {
            payment.setStatus(PaymentStatus.COD);
            payment.setGateway(PaymentGateway.COD);
        }

        Order order = new Order();
        order.setCreateAt(new Date());
        order.setUser(user);
        order.setUserAddress(addressTo);
        order.setPayment(payment);
        order.setEstimatedDeliveryDate(basicShippingOrderInfo.getExpectedDeliveryDate());
        
        orderRepository.save(order);
        
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(book);
        orderItem.setQuantity(1);
        orderItem.setProductPrice(book.getPrice());
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        order.setOrderItems(Arrays.asList(orderItem));

        book.setStock(book.getStock() - 1);
        book.setSold(book.getSold() + 1);
        bookRepository.save(book);


        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(order.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(order, request);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);

    }



}
