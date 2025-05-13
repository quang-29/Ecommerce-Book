package org.example.bookstore.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.bookstore.enums.*;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.*;
import org.example.bookstore.model.address.Address;
import org.example.bookstore.model.payment.Payment;
import org.example.bookstore.model.shipment.BasicShippingOrderInfo;
import org.example.bookstore.model.shipment.ShipmentInfo;
import org.example.bookstore.payload.Note;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.OrderItemDTO;
import org.example.bookstore.payload.order.PlaceOrderDTO;
import org.example.bookstore.payload.response.PlaceOrderResponse;
import org.example.bookstore.repository.*;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.service.Interface.NotificationService;
import org.example.bookstore.service.Interface.OrderService;
import org.example.bookstore.service.Interface.UserAddressService;
import org.example.bookstore.service.firebase.FirebaseMessagingService;
import org.example.bookstore.service.shipment.GHNService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private GHNService ghnService;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final UserAddressService userAddressService;


    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    public OrderServiceImpl(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }


    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderDTO placeOrderDTO, HttpServletRequest httpServletRequest) throws Exception {

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

        Notifications notifications = new Notifications();
        notifications.setReceiver(user);
        notifications.setRead(false);
        notifications.setContent("Đơn hàng %s đã được tạo thành công"+ order.getId());
        notifications.setCreatedAt(new Date());
        notifications.setItemCount(orderItems.size());
        notifications.setScope(NotificationScope.SHOP);
        notificationRepository.save(notifications);
        logger.info("Notifications", notifications);
        notificationService.sendNotification(notifications);



//        Notification notification = Notification.builder()
//                .context("Dat hang thanh cong")
//                .body(String.format("Đơn hàng với id %s đã được tạo thành công", order.getId().toString()))
//                .users(Collections.singletonList(user))
//                .referenceId(order.getId().toString())
//                .title("Đã tạo đơn hàng")
//                .build();
//        user.getNotifications().add(notification);
//        userRepository.save(user);
//        try{
//            firebaseMessagingService
//                    .sendNotification(new Note("Context.ORDER", "ORDER_CREATE_SUCCESS", order.getId().toString()),user.getDeviceToken());
//        } catch (FirebaseMessagingException e) {
//            logger.error(e.getMessage());
//        }
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        placeOrderResponse.setOrderId(order.getId());
        if(paymentType == PaymentType.BANK_TRANSFER){
            String paymentUrl = vnPayService.createPaymentUrl(order, httpServletRequest);
            placeOrderResponse.setPaymentUrl(paymentUrl);
        }
        return placeOrderResponse;
    }

    @Override
    public OrderDTO getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setOrderItem(order.getOrderItems().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class)).collect(Collectors.toList()));
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        // Không throw exception nếu không có đơn hàng
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


    @Override
    public List<OrderDTO> getAllOrders() {
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

    @Override
    public OrderDTO updateOrder(UUID orderId, int orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setOrderStatus(orderStatus);
        return modelMapper.map(orderRepository.save(order), OrderDTO.class);
    }

    @Override
    public String cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

//        if (order.getOrderStatus() != OrderStatus.WAIT_PAYMENT.getValue() ||
//                order.getOrderStatus() != OrderStatus.PAID.getValue()) {
//            throw new AppException(ErrorCode.ORDER_CANCELED_ERROR);
//        }
//
//        order.setOrderStatus(OrderStatus.CANCELLED.getValue());
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);
        for (OrderItem orderItem : orderItems) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());
             bookRepository.save(book);
        }
        return "Order has been canceled successfully.";
    }

    @Override
    public String confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//        order.setOrderStatus(OrderStatus.PROCESSING.getValue());
        orderRepository.save(order);
        return "Confirm order successfully.";
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
