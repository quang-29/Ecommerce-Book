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
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
    private final StoreBookRepository storeBookRepository;
    private final GHNService ghnService;
    private final VNPayService vnPayService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final UserAddressService userAddressService;

    public OrderService(CartRepository cartRepository, UserRepository userRepository, ModelMapper modelMapper, PaymentRepository paymentRepository, OrderRepository orderRepository, CartService cartService, CartItemRepository cartItemRepository, OrderItemRepository orderItemRepository, BookRepository bookRepository, StoreBookRepository storeBookRepository, GHNService ghnService, VNPayService vnPayService, NotificationRepository notificationRepository, NotificationService notificationService, UserAddressService userAddressService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
        this.storeBookRepository = storeBookRepository;
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

        List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartEntityId(cartEntity.getId());
        if (cartItemEntities.isEmpty()) {
            throw new ResourceNotFoundException(MessageException.ORDER_ERROR);
        }

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

        reserveStock(cartItemEntities);

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
            orderItem.setStoreBookEntity(cartItemEntity.getStoreBookEntity());
            orderItem.setQuantity(cartItemEntity.getQuantity());
            orderItem.setProductPrice(cartItemEntity.getBookPrice());
            orderItem.setOrderEntity(orderEntity);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        cartService.clearCart(cartEntity.getId());

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
                .map(this::mapToOrderItemDto).collect(Collectors.toList()));
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
                            .map(this::mapToOrderItemDto)
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

    @Transactional
    public ServerResponseDto cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        PaymentStatus currentStatus = payment.getStatus();
        if (currentStatus == PaymentStatus.CANCELLED) {
            return ServerResponseDto.success("Order has already been canceled");
        }
        if (currentStatus == PaymentStatus.IN_TRANSIT || currentStatus == PaymentStatus.DELIVERED) {
            throw new ResourceNotFoundException(MessageException.ORDER_CANCELED_ERROR);
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        orderEntity.setPayment(payment);
        orderRepository.save(orderEntity);

        List<OrderItem> orderItems = orderItemRepository.findByOrderEntity_Id(orderId);

        Map<Long, Integer> quantityByStoreBookId = orderItems.stream()
                .filter(orderItem -> orderItem.getStoreBookEntity() != null)
                .collect(Collectors.groupingBy(oi -> oi.getStoreBookEntity().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)));

        for (Map.Entry<Long, Integer> entry : quantityByStoreBookId.entrySet()) {
            storeBookRepository.increaseStock(entry.getKey(), entry.getValue().longValue());
        }
        if (currentStatus == PaymentStatus.CONFIRMED || currentStatus == PaymentStatus.COMPLETED) {
            decreaseSoldCount(orderId);
        }
        return ServerResponseDto.success("Order has been canceled successfully");
    }

    @Transactional
    public ServerResponseDto confirmOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = orderEntity.getPayment();
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new ResourceNotFoundException(MessageException.ORDER_CANCELED);
        }
        if (payment.getStatus() == PaymentStatus.CONFIRMED) {
            return ServerResponseDto.success("Order has already been confirmed");
        }
        if (payment.getStatus() != PaymentStatus.CONFIRMED) {
            increaseSoldCount(orderId);
        }
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
        StoreBookEntity storeBookEntity = resolveStoreBook(placeSingleBookDTO.getStoreBookId(), placeSingleBookDTO.getBookId(), placeSingleBookDTO.getStoreId());
        BookEntity bookEntity = storeBookEntity.getBookEntity();
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

        long totalPay = storeBookEntity.getEffectivePrice() + shippingFee;

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

        reserveStock(storeBookEntity.getId(), 1);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateAt(new Date());
        orderEntity.setUser(user);
        orderEntity.setUserAddress(addressTo);
        orderEntity.setPayment(payment);
        orderEntity.setEstimatedDeliveryDate(basicShippingOrderInfo.getExpectedDeliveryDate());
        
        orderRepository.save(orderEntity);
        
        OrderItem orderItem = new OrderItem();
        orderItem.setBookEntity(bookEntity);
        orderItem.setStoreBookEntity(storeBookEntity);
        orderItem.setQuantity(1);
        orderItem.setProductPrice(storeBookEntity.getEffectivePrice());
        orderItem.setOrderEntity(orderEntity);
        orderItemRepository.save(orderItem);
        orderEntity.setOrderItems(Arrays.asList(orderItem));

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(orderEntity.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(orderEntity, request);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);

    }

    private StoreBookEntity resolveStoreBook(Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null) {
            return storeBookRepository.findById(storeBookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        if (storeId != null) {
            return storeBookRepository.findByStoreEntityIdAndBookEntityId(storeId, bookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        return storeBookRepository.findFirstByBookEntityIdAndStockGreaterThanAndActiveTrueOrderByIdAsc(bookId, 0L)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM));
    }

    private void reserveStock(List<CartItemEntity> cartItemEntities) {
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            if (cartItemEntity.getStoreBookEntity() == null) {
                throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
            }
            reserveStock(cartItemEntity.getStoreBookEntity().getId(), cartItemEntity.getQuantity());
        }
    }

    private void reserveStock(Long storeBookId, Integer quantity) {
        int updatedRows = storeBookRepository.decreaseStockIfAvailable(storeBookId, quantity.longValue());
        if (updatedRows == 0) {
            throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
        }
    }

    private void increaseSoldCount(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderEntity_Id(orderId);
        Map<Long, Integer> quantityByBookId = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getBookEntity().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)));
        List<BookEntity> bookEntities = bookRepository.findAllById(quantityByBookId.keySet());
        for (BookEntity bookEntity : bookEntities) {
            bookEntity.setSold(bookEntity.getSold() + quantityByBookId.get(bookEntity.getId()));
        }
        bookRepository.saveAll(bookEntities);
    }

    private void decreaseSoldCount(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderEntity_Id(orderId);
        Map<Long, Integer> quantityByBookId = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getBookEntity().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)));
        List<BookEntity> bookEntities = bookRepository.findAllById(quantityByBookId.keySet());
        for (BookEntity bookEntity : bookEntities) {
            long sold = bookEntity.getSold() == null ? 0L : bookEntity.getSold();
            bookEntity.setSold(Math.max(0L, sold - quantityByBookId.get(bookEntity.getId())));
        }
        bookRepository.saveAll(bookEntities);
    }

    private OrderItemDTO mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
        if (orderItem.getStoreBookEntity() != null) {
            orderItemDTO.setStoreId(orderItem.getStoreBookEntity().getStoreEntity().getId());
            orderItemDTO.setStoreName(orderItem.getStoreBookEntity().getStoreEntity().getName());
        }
        return orderItemDTO;
    }



}
