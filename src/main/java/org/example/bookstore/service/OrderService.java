package org.example.bookstore.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.example.bookstore.config.dto.ServerResponseDto;
import org.example.bookstore.enums.*;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.*;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.OrderItemDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.order.PlaceSingleBookDTO;
import org.example.bookstore.payload.response.PlaceOrderResponse;
import org.example.bookstore.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    private final StoreRepository storeRepository;
    private final StoreVoucherRepository storeVoucherRepository;
    private final VNPayService vnPayService;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private static final long FLAT_SHIPPING_FEE = 30_000L;
    private static final int DEFAULT_DELIVERY_DAYS = 3;
    private static final long PENDING_PAYMENT_CHECK_INTERVAL_MS = 60_000L;

    private final EmailService emailService;

    public OrderService(CartRepository cartRepository, UserRepository userRepository, ModelMapper modelMapper, PaymentRepository paymentRepository, OrderRepository orderRepository, CartService cartService, CartItemRepository cartItemRepository, OrderItemRepository orderItemRepository, BookRepository bookRepository, StoreBookRepository storeBookRepository, StoreRepository storeRepository, StoreVoucherRepository storeVoucherRepository, VNPayService vnPayService, NotificationRepository notificationRepository, NotificationService notificationService, ObjectMapper objectMapper, EmailService emailService) {
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
        this.storeRepository = storeRepository;
        this.storeVoucherRepository = storeVoucherRepository;
        this.vnPayService = vnPayService;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @Transactional
    public ServerResponseDto placeOrder(PlaceOrderDTO placeOrderDTO, HttpServletRequest httpServletRequest) throws Exception {

        CartEntity cartEntity = cartRepository.findById(placeOrderDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.CART_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        if (!Objects.equals(cartEntity.getUserId(), user.getId())) {
            throw new ResourceNotFoundException(MessageException.UNAUTHORIZED_ACTION);
        }

        List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartId(cartEntity.getId());
        if (cartItemEntities.isEmpty()) {
            throw new ResourceNotFoundException(MessageException.ORDER_ERROR);
        }

        long allBookPrice = cartItemEntities.stream()
                .mapToLong(item -> item.getBookPrice() * item.getQuantity())
                .sum();
        long voucherDiscount = calculateVoucherDiscount(cartItemEntities, placeOrderDTO.getVoucherCodes());

        String shippingAddress = placeOrderDTO.getShippingAddress();
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new ResourceNotFoundException(MessageException.ADDRESS_NOT_FOUND);
        }

        long shippingFee = FLAT_SHIPPING_FEE;

        long totalPay = Math.max(0, allBookPrice - voucherDiscount) + shippingFee;

        PaymentType paymentType = placeOrderDTO.getPaymentType();

        if(paymentType == null) {
            throw new ResourceNotFoundException(MessageException.PAYMENT_METHOD_NOT_FOUND);
        }

        Payment payment = new Payment();
        payment.setType(paymentType);
        payment.setCreatedAt(new Date());
        payment.setFeeShip(shippingFee);
        payment.setDiscountAmount(voucherDiscount);
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

        paymentRepository.save(payment);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateAt(new Date());
        orderEntity.setUserId(user.getId());
        orderEntity.setShippingAddress(shippingAddress);
        orderEntity.setPaymentId(payment.getId());
        orderEntity.setEstimatedDeliveryDate(estimatedDeliveryDate());
        orderRepository.save(orderEntity);

        List<OrderItem> orderItems = new ArrayList<>();
        Map<Long, BookEntity> booksById = loadBooks(cartItemEntities.stream().map(CartItemEntity::getBookId).collect(Collectors.toList()));
        Map<Long, StoreBookEntity> storeBooksById = loadStoreBooks(cartItemEntities.stream().map(CartItemEntity::getStoreBookId).collect(Collectors.toList()));

        for (CartItemEntity cartItemEntity : cartItemEntities) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(cartItemEntity.getBookId());
            orderItem.setStoreBookId(cartItemEntity.getStoreBookId());
            orderItem.setQuantity(cartItemEntity.getQuantity());
            orderItem.setProductPrice(cartItemEntity.getBookPrice());
            if (cartItemEntity.getStoreBookId() != null) {
                BookEntity itemBook = booksById.get(cartItemEntity.getBookId());
                StoreBookEntity itemStoreBook = storeBooksById.get(cartItemEntity.getStoreBookId());
                if (itemBook != null && itemStoreBook != null) {
                    orderItem.setDiscountPercent(itemStoreBook.getDiscountPercent(itemBook));
                    orderItem.setDiscountAmount(itemStoreBook.getDiscountAmount(itemBook));
                }
            }
            orderItem.setOrderId(orderEntity.getId());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        cartService.clearCart(cartEntity.getId());

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Xác nhận đơn hàng #" + orderEntity.getId(),
                buildOrderConfirmationEmail(user, orderEntity, orderItems, payment)
        );

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
        OrderDTO orderDTO = mapToOrderDtos(List.of(orderEntity)).get(0);
        return ServerResponseDto.success(orderDTO);
    }

    public ServerResponseDto getOrdersByUserId(Long userId, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderEntity> orderPage = orderRepository.findAllOrderByUserId(userId, pageable);
        Page<OrderDTO> orderDTOPage = new PageImpl<>(mapToOrderDtos(orderPage.getContent()), pageable, orderPage.getTotalElements());
        return ServerResponseDto.success(orderDTOPage);
    }


    public List<OrderDTO> getAllOrders(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<OrderEntity> orderPage = orderRepository.findAll(pageable);
        if (orderPage.getContent().isEmpty()) {
            throw new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND);
        }
        return mapToOrderDtos(orderPage.getContent());
    }

    // Batch-loads everything the DTOs need (items, books, store_books, stores, payments) with one
    // query per table, instead of several queries per order and per order item.
    private List<OrderDTO> mapToOrderDtos(List<OrderEntity> orders) {
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, List<OrderItem>> itemsByOrderId = orderItemRepository
                .findByOrderIdIn(orders.stream().map(OrderEntity::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
        List<OrderItem> allItems = itemsByOrderId.values().stream().flatMap(List::stream).collect(Collectors.toList());
        Map<Long, BookEntity> booksById = loadBooks(allItems.stream().map(OrderItem::getBookId).collect(Collectors.toList()));
        Map<Long, StoreBookEntity> storeBooksById = loadStoreBooks(allItems.stream().map(OrderItem::getStoreBookId).collect(Collectors.toList()));
        Map<Long, StoreEntity> storesById = storeRepository
                .findAllById(storeBooksById.values().stream().map(StoreBookEntity::getStoreId).distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(StoreEntity::getId, s -> s));
        Map<Long, Payment> paymentsById = paymentRepository
                .findAllById(orders.stream().map(OrderEntity::getPaymentId).filter(Objects::nonNull)
                        .map(Long::intValue).distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Payment::getId, p -> p));

        List<OrderDTO> result = new ArrayList<>();
        for (OrderEntity order : orders) {
            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            orderDTO.setOrderItem(itemsByOrderId.getOrDefault(order.getId(), List.of()).stream()
                    .map(item -> mapToOrderItemDto(item, booksById, storeBooksById, storesById))
                    .collect(Collectors.toList()));
            if (order.getPaymentId() != null) {
                Payment payment = paymentsById.get(order.getPaymentId());
                if (payment != null) {
                    orderDTO.setPayment(modelMapper.map(payment, org.example.bookstore.payload.payment.PaymentDTO.class));
                }
            }
            result.add(orderDTO);
        }
        return result;
    }

    @Transactional
    public ServerResponseDto updateStatusOrder(Long orderId, int orderStatus) {
        PaymentStatus[] statuses = PaymentStatus.values();
        if (orderStatus < 0 || orderStatus >= statuses.length) {
            throw new ResourceNotFoundException(MessageException.INVALID_REQUEST);
        }
        switch (statuses[orderStatus]) {
            case CONFIRMED:
                return confirmOrder(orderId);
            case IN_TRANSIT:
                return transitOrder(orderId);
            case DELIVERED:
                return deliveryOrder(orderId);
            case CANCELLED:
                return cancelOrder(orderId);
            default:
                throw new ResourceNotFoundException(MessageException.INVALID_REQUEST);
        }
    }

    @Transactional
    public ServerResponseDto cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = getPaymentOf(orderEntity);
        PaymentStatus currentStatus = payment.getStatus();
        if (currentStatus == PaymentStatus.CANCELLED) {
            return ServerResponseDto.success("Order has already been canceled");
        }
        if (currentStatus == PaymentStatus.IN_TRANSIT || currentStatus == PaymentStatus.DELIVERED) {
            throw new ResourceNotFoundException(MessageException.ORDER_CANCELED_ERROR);
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);

        restoreStockForOrder(orderId);
        if (currentStatus == PaymentStatus.CONFIRMED || currentStatus == PaymentStatus.COMPLETED) {
            decreaseSoldCount(orderId);
        }
        return ServerResponseDto.success("Order has been canceled successfully");
    }

    @Transactional
    public ServerResponseDto confirmOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = getPaymentOf(orderEntity);
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
        paymentRepository.save(payment);
        return ServerResponseDto.success("Confirm order successfully");
    }

    public ServerResponseDto transitOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = getPaymentOf(orderEntity);
        payment.setStatus(PaymentStatus.IN_TRANSIT);
        paymentRepository.save(payment);
        return ServerResponseDto.success("Start delivery order");
    }

    public ServerResponseDto deliveryOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.ORDER_NOT_FOUND));
        Payment payment = getPaymentOf(orderEntity);
        payment.setStatus(PaymentStatus.DELIVERED);
        paymentRepository.save(payment);
        return ServerResponseDto.success("Delivery order successfully");
    }

    private Payment getPaymentOf(OrderEntity orderEntity) {
        return paymentRepository.findById(orderEntity.getPaymentId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.PAYMENT_NOT_FOUND));
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
        BookEntity bookEntity = bookRepository.findById(storeBookEntity.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.USER_NOT_FOUND));

        String shippingAddress = placeSingleBookDTO.getShippingAddress();
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new ResourceNotFoundException(MessageException.INVALID_ADDRESS);
        }

        long shippingFee = FLAT_SHIPPING_FEE;

        long voucherDiscount = calculateVoucherDiscount(storeBookEntity, placeSingleBookDTO.getVoucherCode());
        long totalPay = Math.max(0, storeBookEntity.getEffectivePrice(bookEntity) - voucherDiscount) + shippingFee;

        PaymentType paymentType = placeSingleBookDTO.getPaymentType();

        if(paymentType == null) {
            throw new ResourceNotFoundException(MessageException.PAYMENT_METHOD_NOT_FOUND);
        }

        Payment payment = new Payment();
        payment.setType(paymentType);
        payment.setCreatedAt(new Date());
        payment.setFeeShip(shippingFee);
        payment.setDiscountAmount(voucherDiscount);
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

        paymentRepository.save(payment);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateAt(new Date());
        orderEntity.setUserId(user.getId());
        orderEntity.setShippingAddress(shippingAddress);
        orderEntity.setPaymentId(payment.getId());
        orderEntity.setEstimatedDeliveryDate(estimatedDeliveryDate());

        orderRepository.save(orderEntity);

        OrderItem orderItem = new OrderItem();
        orderItem.setBookId(bookEntity.getId());
        orderItem.setStoreBookId(storeBookEntity.getId());
        orderItem.setQuantity(1);
        orderItem.setProductPrice(storeBookEntity.getEffectivePrice(bookEntity));
        orderItem.setDiscountPercent(storeBookEntity.getDiscountPercent(bookEntity));
        orderItem.setDiscountAmount(storeBookEntity.getDiscountAmount(bookEntity));
        orderItem.setOrderId(orderEntity.getId());
        orderItemRepository.save(orderItem);

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(orderEntity.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(orderEntity, request);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return ServerResponseDto.success(placeOrderResponse);

    }

    @Data
    class OrderRequest {
        private List<OrderItemRequest> items;
    }

    @Data
    class OrderItemRequest {
        private Long bookId;
        private Long storeId;
        private Integer quantity;
    }


    @Transactional
    public ServerResponseDto order(OrderRequest orderRequest, Long voucherId, HttpServletRequest request) throws Exception {
        List<OrderItemRequest> items = orderRequest.getItems();
        Set<Long> listBookIds = items.stream().map(OrderItemRequest::getBookId).collect(Collectors.toSet());
        Set<Long> listStoreIds = items.stream().map(OrderItemRequest::getStoreId).collect(Collectors.toSet());

        Map<Long, BookEntity> bookMap = bookRepository.findAllById(listBookIds).stream()
                .collect(Collectors.toMap(BookEntity::getId, Function.identity()));
        Map<Long, StoreBookEntity> storeMap = storeBookRepository.findAllById(listStoreIds).stream()
                .collect(Collectors.toMap(StoreBookEntity::getId, Function.identity()));


        if(listBookIds.isEmpty()){

        }
        OrderEntity orderEntity = new OrderEntity();

        for(OrderItemRequest item: items){
            BookEntity book = bookMap.get(item.getBookId());
            StoreBookEntity store = storeMap.get(item.getStoreId());
            if (book == null){
                return ServerResponseDto.error("Book not found: " + item.getBookId());
            }
            if (store == null) {
                return ServerResponseDto.error("Store not found: " + item.getStoreId());
            }
            if(item.quantity > book.getStock()) {
                return ServerResponseDto.error("Book is out of stock");
            }


            OrderItem orderItem = new OrderItem();



        }


//        long shippingFee = FLAT_SHIPPING_FEE;
//
//        long voucherDiscount = calculateVoucherDiscount(storeBookEntity, placeSingleBookDTO.getVoucherCode());
//        long totalPay = Math.max(0, storeBookEntity.getEffectivePrice(bookEntity) - voucherDiscount) + shippingFee;
//
//        PaymentType paymentType = placeSingleBookDTO.getPaymentType();
//
//        if(paymentType == null) {
//            throw new ResourceNotFoundException(MessageException.PAYMENT_METHOD_NOT_FOUND);
//        }

//        Payment payment = new Payment();
//        payment.setType(paymentType);
//        payment.setCreatedAt(new Date());
//        payment.setFeeShip(shippingFee);
//        payment.setDiscountAmount(voucherDiscount);
//        payment.setAmount(totalPay);
//        if(payment.getType() != PaymentType.COD) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(payment.getCreatedAt());
//            calendar.add(Calendar.MINUTE, 2);
//            payment.setExpireAt(calendar.getTime());
//            payment.setStatus(PaymentStatus.PENDING);
//            payment.setGateway(PaymentGateway.VNPAY);
//        }
//        else {
//            payment.setStatus(PaymentStatus.COD);
//            payment.setGateway(PaymentGateway.COD);
//        }
//
//        reserveStock(storeBookEntity.getId(), 1);
//
//        paymentRepository.save(payment);
//
//        OrderEntity orderEntity = new OrderEntity();
//        orderEntity.setCreateAt(new Date());
//        orderEntity.setUserId(user.getId());
//        orderEntity.setShippingAddress(shippingAddress);
//        orderEntity.setPaymentId(payment.getId());
//        orderEntity.setEstimatedDeliveryDate(estimatedDeliveryDate());
//
//        orderRepository.save(orderEntity);
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setBookId(bookEntity.getId());
//        orderItem.setStoreBookId(storeBookEntity.getId());
//        orderItem.setQuantity(1);
//        orderItem.setProductPrice(storeBookEntity.getEffectivePrice(bookEntity));
//        orderItem.setDiscountPercent(storeBookEntity.getDiscountPercent(bookEntity));
//        orderItem.setDiscountAmount(storeBookEntity.getDiscountAmount(bookEntity));
//        orderItem.setOrderId(orderEntity.getId());
//        orderItemRepository.save(orderItem);
//
//        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
//        placeOrderResponse.setOrderId(orderEntity.getId());
//        if(paymentType == PaymentType.BANK_TRANSFER){
//            String paymentUrl = vnPayService.createPaymentUrl(orderEntity, request);
//            placeOrderResponse.setPaymentUrl(paymentUrl);
//        }
        return ServerResponseDto.success("");

    }

    private Date estimatedDeliveryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, DEFAULT_DELIVERY_DAYS);
        return calendar.getTime();
    }

    private StoreBookEntity resolveStoreBook(Long storeBookId, Long bookId, Long storeId) {
        if (storeBookId != null) {
            return storeBookRepository.findById(storeBookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        if (storeId != null) {
            return storeBookRepository.findByStoreIdAndBookId(storeId, bookId)
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        }
        return storeBookRepository.findFirstByBookIdAndStockGreaterThanAndActiveTrueOrderByIdAsc(bookId, 0L)
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM));
    }

    private void reserveStock(List<CartItemEntity> cartItemEntities) {
        for (CartItemEntity cartItemEntity : cartItemEntities) {
            if (cartItemEntity.getStoreBookId() == null) {
                throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
            }
            reserveStock(cartItemEntity.getStoreBookId(), cartItemEntity.getQuantity());
        }
    }

    private void reserveStock(Long storeBookId, Integer quantity) {
        int updatedRows = storeBookRepository.decreaseStockIfAvailable(storeBookId, quantity.longValue());
        if (updatedRows == 0) {
            throw new ResourceNotFoundException(MessageException.BOOK_STOCK_PROBLEM);
        }
    }

    private void restoreStockForOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        Map<Long, Integer> quantityByStoreBookId = orderItems.stream()
                .filter(orderItem -> orderItem.getStoreBookId() != null)
                .collect(Collectors.groupingBy(OrderItem::getStoreBookId,
                        Collectors.summingInt(OrderItem::getQuantity)));
        for (Map.Entry<Long, Integer> entry : quantityByStoreBookId.entrySet()) {
            storeBookRepository.increaseStock(entry.getKey(), entry.getValue().longValue());
        }
    }

    @Scheduled(fixedDelay = PENDING_PAYMENT_CHECK_INTERVAL_MS)
    @Transactional
    public void expireStalePendingPayments() {
        Date now = new Date();
        List<Payment> pendingPayments = paymentRepository.findAllByStatusAndType(PaymentStatus.PENDING, PaymentType.BANK_TRANSFER);
        for (Payment payment : pendingPayments) {
            if (payment.getExpireAt() == null || payment.getExpireAt().after(now)) {
                continue;
            }
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
            orderRepository.findByPaymentId((long) payment.getId())
                    .ifPresent(order -> restoreStockForOrder(order.getId()));
            LOGGER.info("Expired pending payment {} and restored stock", payment.getId());
        }
    }

    private void increaseSoldCount(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        Map<Long, Integer> quantityByBookId = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getBookId,
                        Collectors.summingInt(OrderItem::getQuantity)));
        List<BookEntity> bookEntities = bookRepository.findAllById(quantityByBookId.keySet());
        for (BookEntity bookEntity : bookEntities) {
            bookEntity.setSold(bookEntity.getSold() + quantityByBookId.get(bookEntity.getId()));
        }
        bookRepository.saveAll(bookEntities);
    }

    private void decreaseSoldCount(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        Map<Long, Integer> quantityByBookId = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getBookId,
                        Collectors.summingInt(OrderItem::getQuantity)));
        List<BookEntity> bookEntities = bookRepository.findAllById(quantityByBookId.keySet());
        for (BookEntity bookEntity : bookEntities) {
            long sold = bookEntity.getSold() == null ? 0L : bookEntity.getSold();
            bookEntity.setSold(Math.max(0L, sold - quantityByBookId.get(bookEntity.getId())));
        }
        bookRepository.saveAll(bookEntities);
    }

    private long calculateVoucherDiscount(List<CartItemEntity> cartItemEntities, Map<Long, String> voucherCodes) {
        if (voucherCodes == null || voucherCodes.isEmpty()) {
            return 0L;
        }

        Map<Long, StoreBookEntity> storeBooksById = loadStoreBooks(cartItemEntities.stream().map(CartItemEntity::getStoreBookId).collect(Collectors.toList()));
        Map<Long, Long> subtotalByStoreId = cartItemEntities.stream()
                .filter(item -> item.getStoreBookId() != null)
                .collect(Collectors.groupingBy(item -> Optional.ofNullable(storeBooksById.get(item.getStoreBookId()))
                                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND)).getStoreId(),
                        Collectors.summingLong(item -> item.getBookPrice() * item.getQuantity())));

        long voucherDiscount = 0L;
        for (Map.Entry<Long, String> entry : voucherCodes.entrySet()) {
            Long storeId = entry.getKey();
            String voucherCode = entry.getValue();
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                continue;
            }
            long storeSubtotal = subtotalByStoreId.getOrDefault(storeId, 0L);
            if (storeSubtotal <= 0) {
                throw new ResourceNotFoundException(MessageException.VOUCHER_INVALID);
            }
            StoreVoucherEntity voucher = storeVoucherRepository.findByStoreIdAndVoucherCodeIgnoreCase(storeId, voucherCode.trim())
                    .orElseThrow(() -> new ResourceNotFoundException(MessageException.VOUCHER_NOT_FOUND));
            if (!voucher.isUsableNow()) {
                throw new ResourceNotFoundException(MessageException.VOUCHER_INVALID);
            }
            voucherDiscount += voucher.calculateDiscount(storeSubtotal);
        }
        return voucherDiscount;
    }

    private long calculateVoucherDiscount(StoreBookEntity storeBookEntity, String voucherCode) {
        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            return 0L;
        }
        StoreVoucherEntity voucher = storeVoucherRepository.findByStoreIdAndVoucherCodeIgnoreCase(
                        storeBookEntity.getStoreId(), voucherCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.VOUCHER_NOT_FOUND));
        if (!voucher.isUsableNow()) {
            throw new ResourceNotFoundException(MessageException.VOUCHER_INVALID);
        }
        BookEntity bookEntity = bookRepository.findById(storeBookEntity.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageException.BOOK_NOT_FOUND));
        return voucher.calculateDiscount(storeBookEntity.getEffectivePrice(bookEntity));
    }

    private OrderItemDTO mapToOrderItemDto(OrderItem orderItem, Map<Long, BookEntity> booksById,
                                           Map<Long, StoreBookEntity> storeBooksById, Map<Long, StoreEntity> storesById) {
        OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
        BookEntity book = booksById.get(orderItem.getBookId());
        if (book != null) {
            orderItemDTO.setBook(modelMapper.map(book, org.example.bookstore.payload.BookDTO.class));
        }
        StoreBookEntity storeBook = orderItem.getStoreBookId() == null ? null : storeBooksById.get(orderItem.getStoreBookId());
        if (storeBook != null) {
            StoreEntity store = storesById.get(storeBook.getStoreId());
            if (store != null) {
                orderItemDTO.setStoreId(store.getId());
                orderItemDTO.setStoreName(store.getName());
            }
        }
        return orderItemDTO;
    }

    private Map<Long, BookEntity> loadBooks(Collection<Long> ids) {
        return bookRepository.findAllById(ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(BookEntity::getId, b -> b));
    }

    private Map<Long, StoreBookEntity> loadStoreBooks(Collection<Long> ids) {
        return storeBookRepository.findAllById(ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(StoreBookEntity::getId, sb -> sb));
    }

    private String buildOrderConfirmationEmail(UserEntity user, OrderEntity orderEntity, List<OrderItem> orderItems, Payment payment) {
        StringBuilder body = new StringBuilder();
        body.append("Xin chào ").append(user.getFirstName()).append(",\n\n");
        body.append("Đơn hàng #").append(orderEntity.getId()).append(" của bạn đã được ghi nhận thành công.\n\n");
        body.append("Chi tiết đơn hàng:\n");
        Map<Long, BookEntity> booksById = loadBooks(orderItems.stream().map(OrderItem::getBookId).collect(Collectors.toList()));
        for (OrderItem item : orderItems) {
            BookEntity book = booksById.get(item.getBookId());
            String title = book == null ? "" : book.getTitle();
            body.append("- ").append(title)
                    .append(" x").append(item.getQuantity())
                    .append(": ").append(item.getProductPrice() * item.getQuantity()).append(" VND\n");
        }
        body.append("\nPhí vận chuyển: ").append(payment.getFeeShip()).append(" VND\n");
        if (payment.getDiscountAmount() > 0) {
            body.append("Giảm giá: ").append(payment.getDiscountAmount()).append(" VND\n");
        }
        body.append("Tổng thanh toán: ").append(payment.getAmount()).append(" VND\n");
        body.append("Phương thức thanh toán: ").append(payment.getType()).append("\n\n");
        body.append("Cảm ơn bạn đã mua hàng tại BookStore!");
        return body.toString();
    }

}
