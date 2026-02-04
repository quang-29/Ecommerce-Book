package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.*;
import org.example.bookstore.exception.ResourceNotFoundException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

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

        CartEntity cartEntity = cartRepository.findById(placeOrderDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        List<CartItemEntity> cartItemEntities = cartEntity.getCartItemEntities();

        long allBookPrice = cartItemEntities.stream()
                .mapToLong(item -> item.getBookPrice() * item.getQuantity())
                .sum();

        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        if (placeOrderDTO.getAddressId() == null) {
            throw new ResourceNotFoundException(MessageException.ADDRESS_NOT_FOUND);
        }
        UserAddress addressTo = userAddressList.stream()
                .filter(address -> address.getId().equals(placeOrderDTO.getAddressId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ADDRESS_NOT_FOUND));

        ShipmentInfo shipmentInfo = ShipmentInfo.builder()
                .from(new ShopAddress())
                .to(addressTo)
                .weight(placeOrderDTO.getWeight())
                .build();

        BasicShippingOrderInfo basicShippingOrderInfo = ghnService.calculateShipmentFee(shipmentInfo);
        long shippingFee = basicShippingOrderInfo.getFee();

        long totalPay = allBookPrice + shippingFee;

        PaymentType paymentType = placeOrderDTO.getPaymentType();

        if(paymentType == null) {
            throw new ResourceNotFoundException(MessageException.PAYMENT_METHOD_NOT_FOUND);
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

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateAt(new Date());
        orderEntity.setUser(user);
        orderEntity.setUserAddress(addressTo);
        orderEntity.setPayment(payment);
        orderEntity.setEstimatedDeliveryDate(basicShippingOrderInfo.getExpectedDeliveryDate());
        orderRepository.save(orderEntity);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemEntity cartItemEntity : cartItemEntities) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBookEntity(cartItemEntity.getBookEntity());
            orderItem.setQuantity(cartItemEntity.getQuantity());
            orderItem.setProductPrice(cartItemEntity.getBookPrice());
            orderItem.setOrderEntity(orderEntity);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        for (int i = 0; i < cartEntity.getCartItemEntities().size(); i++) {
            CartItemEntity cartItemEntity1 = cartEntity.getCartItemEntities().get(i);
            int quantity = cartItemEntity1.getQuantity();
            BookEntity bookEntity = cartItemEntity1.getBookEntity();
            cartService.deleteProductFromCart(cartEntity.getId(), bookEntity.getId());
            bookEntity.setStock(bookEntity.getStock() - quantity);
            bookEntity.setSold(bookEntity.getSold() + quantity);
            bookRepository.save(bookEntity);
        }

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(orderEntity.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(orderEntity, httpServletRequest);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);
    }

    public ServerResponseDto getOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);
        orderDTO.setOrderItem(orderEntity.getOrderItems().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class)).collect(Collectors.toList()));
        return ServerResponseDto.success(orderDTO);
    }

    public ServerResponseDto getOrdersByUserId(Long userId, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orderDTOPage = orderRepository.findAllOrderByUserId(userId, pageable).map(order -> modelMapper.map(order, OrderDTO.class));
        return ServerResponseDto.success(orderDTOPage);
    }


    public List<OrderDTO> getAllOrders(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<OrderEntity> orderPage = orderRepository.findAll(pageable);
        if (orderPage.getContent().isEmpty()) {
            throw new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND);
        }
        return orderPage.getContent().stream()
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
    public ServerResponseDto updateStatusOrder(Long orderId, int orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
//        order.setOrderStatus(orderStatus);
        return ServerResponseDto.success(modelMapper.map(orderRepository.save(orderEntity), OrderDTO.class));
    }

    public ServerResponseDto cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        payment.setStatus(PaymentStatus.CANCELLED);
        orderEntity.setPayment(payment);
        orderRepository.save(orderEntity);

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);

        Map<Long, Integer> quantityByBookId = orderItems.stream()
                .collect(Collectors.groupingBy(oi -> oi.getBookEntity().getId(),
                         Collectors.summingInt(OrderItem::getQuantity)));

        List<BookEntity> bookEntities = bookRepository.findAllById(quantityByBookId.keySet());
        for (BookEntity bookEntity : bookEntities) {
            bookEntity.setStock(bookEntity.getStock() + quantityByBookId.get(bookEntity.getId()));
        }
        bookRepository.saveAll(bookEntities);
        return ServerResponseDto.success("Order has been canceled successfully");
    }

    public ServerResponseDto confirmOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        payment.setStatus(PaymentStatus.CONFIRMED);
        orderEntity.setPayment(payment);
        orderRepository.save(orderEntity);
        return ServerResponseDto.success("Confirm order successfully");
    }

    public ServerResponseDto transitOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        payment.setStatus(PaymentStatus.IN_TRANSIT);
        orderEntity.setPayment(payment);
        orderRepository.save(orderEntity);
        return ServerResponseDto.success("Start delivery order");
    }

    public ServerResponseDto deliveryOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        payment.setStatus(PaymentStatus.DELIVERED);
        orderEntity.setPayment(payment);
        orderRepository.save(orderEntity);
        return ServerResponseDto.success("Delivery order successfully");
    }

    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    public ServerResponseDto buyNow(PlaceSingleBookDTO placeSingleBookDTO, HttpServletRequest request) throws Exception {
        BookEntity bookEntity = bookRepository.findById(placeSingleBookDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        List<UserAddress> userAddressList = userAddressService.getAddressListByUser(username);
        if (placeSingleBookDTO.getAddressId() == null) {
            throw new ResourceNotFoundException(MessageException.INVALID_ADDRESS);
        }
        UserAddress addressTo = userAddressList.stream()
                .filter(address -> address.getId().equals(placeSingleBookDTO.getAddressId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ADDRESS_NOT_FOUND));

        ShipmentInfo shipmentInfo = ShipmentInfo.builder()
                .from(new ShopAddress())
                .to(addressTo)
                .weight(placeSingleBookDTO.getWeight())
                .build();

        BasicShippingOrderInfo basicShippingOrderInfo = ghnService.calculateShipmentFee(shipmentInfo);
        long shippingFee = basicShippingOrderInfo.getFee();

        long totalPay = bookEntity.getPrice() + shippingFee;

        PaymentType paymentType = placeSingleBookDTO.getPaymentType();

        if(paymentType == null) {
            throw new ResourceNotFoundException(MessageException.PAYMENT_METHOD_NOT_FOUND);
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

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateAt(new Date());
        orderEntity.setUser(user);
        orderEntity.setUserAddress(addressTo);
        orderEntity.setPayment(payment);
        orderEntity.setEstimatedDeliveryDate(basicShippingOrderInfo.getExpectedDeliveryDate());
        
        orderRepository.save(orderEntity);
        
        OrderItem orderItem = new OrderItem();
        orderItem.setBookEntity(bookEntity);
        orderItem.setQuantity(1);
        orderItem.setProductPrice(bookEntity.getPrice());
        orderItem.setOrderEntity(orderEntity);
        orderItemRepository.save(orderItem);
        orderEntity.setOrderItems(Arrays.asList(orderItem));

        bookEntity.setStock(bookEntity.getStock() - 1);
        bookEntity.setSold(bookEntity.getSold() + 1);
        bookRepository.save(bookEntity);

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(orderEntity.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(orderEntity, request);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);

    }



}
