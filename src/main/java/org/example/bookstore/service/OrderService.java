package org.example.bookstore.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private final StoreRepository storeRepository;
    private final StoreVoucherRepository storeVoucherRepository;
    private final VNPayService vnPayService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private static final long FLAT_SHIPPING_FEE = 30_000L;
    private static final int DEFAULT_DELIVERY_DAYS = 3;

    private final EmailService emailService;

    public OrderService(CartRepository cartRepository, UserRepository userRepository, ModelMapper modelMapper, PaymentRepository paymentRepository, OrderRepository orderRepository, CartService cartService, CartItemRepository cartItemRepository, OrderItemRepository orderItemRepository, BookRepository bookRepository, StoreBookRepository storeBookRepository, StoreRepository storeRepository, StoreVoucherRepository storeVoucherRepository, VNPayService vnPayService, NotificationRepository notificationRepository, NotificationService notificationService, EmailService emailService) {
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

        for (CartItemEntity cartItemEntity : cartItemEntities) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(cartItemEntity.getBookId());
            orderItem.setStoreBookId(cartItemEntity.getStoreBookId());
            orderItem.setQuantity(cartItemEntity.getQuantity());
            orderItem.setProductPrice(cartItemEntity.getBookPrice());
            if (cartItemEntity.getStoreBookId() != null) {
                BookEntity itemBook = bookRepository.findById(cartItemEntity.getBookId()).orElse(null);
                StoreBookEntity itemStoreBook = storeBookRepository.findById(cartItemEntity.getStoreBookId()).orElse(null);
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
        OrderDTO orderDTO = mapToOrderDto(orderEntity);
        return ServerResponseDto.success(orderDTO);
    }

    public ServerResponseDto getOrdersByUserId(Long userId, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orderDTOPage = orderRepository.findAllOrderByUserId(userId, pageable).map(this::mapToOrderDto);
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
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDTO mapToOrderDto(OrderEntity order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setOrderItem(orderItemRepository.findByOrderId(order.getId()).stream()
                .map(this::mapToOrderItemDto).collect(Collectors.toList()));
        if (order.getPaymentId() != null) {
            paymentRepository.findById(order.getPaymentId().intValue())
                    .ifPresent(payment -> orderDTO.setPayment(modelMapper.map(payment, org.example.bookstore.payload.payment.PaymentDTO.class)));
        }
        return orderDTO;
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

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        Map<Long, Integer> quantityByStoreBookId = orderItems.stream()
                .filter(orderItem -> orderItem.getStoreBookId() != null)
                .collect(Collectors.groupingBy(OrderItem::getStoreBookId,
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

        Map<Long, Long> subtotalByStoreId = cartItemEntities.stream()
                .filter(item -> item.getStoreBookId() != null)
                .collect(Collectors.groupingBy(item -> storeBookRepository.findById(item.getStoreBookId())
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

    private OrderItemDTO mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
        bookRepository.findById(orderItem.getBookId())
                .ifPresent(book -> orderItemDTO.setBook(modelMapper.map(book, org.example.bookstore.payload.BookDTO.class)));
        if (orderItem.getStoreBookId() != null) {
            storeBookRepository.findById(orderItem.getStoreBookId()).ifPresent(storeBookEntity ->
                    storeRepository.findById(storeBookEntity.getStoreId()).ifPresent(store -> {
                        orderItemDTO.setStoreId(store.getId());
                        orderItemDTO.setStoreName(store.getName());
                    }));
        }
        return orderItemDTO;
    }

    private String buildOrderConfirmationEmail(UserEntity user, OrderEntity orderEntity, List<OrderItem> orderItems, Payment payment) {
        StringBuilder body = new StringBuilder();
        body.append("Xin chào ").append(user.getFirstName()).append(",\n\n");
        body.append("Đơn hàng #").append(orderEntity.getId()).append(" của bạn đã được ghi nhận thành công.\n\n");
        body.append("Chi tiết đơn hàng:\n");
        for (OrderItem item : orderItems) {
            String title = bookRepository.findById(item.getBookId()).map(BookEntity::getTitle).orElse("");
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
