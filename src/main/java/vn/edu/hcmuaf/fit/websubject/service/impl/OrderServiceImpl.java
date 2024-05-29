package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Address;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;
import vn.edu.hcmuaf.fit.websubject.entity.Order;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime;
import vn.edu.hcmuaf.fit.websubject.repository.*;
import vn.edu.hcmuaf.fit.websubject.service.OrderService;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ORDER_CODE_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();

    @Override
    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getLatestOrder(Integer userId) {
        return orderRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    @Override
    public Order createOrder(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        order.setUser(user);
        order.setOrderCode(generateOrderCode());
        order.setOrderDate(CurrentTime.getCurrentTimeInVietnam());
        if (order.getPaymentMethod().equals("cashondelivery")) {
            order.setPaymentMethod("Thanh toán khi nhận hàng");
        }
        return orderRepository.save(order);
    }

    @Override
    public void createOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public String generateOrderCode() {
        StringBuilder sb = new StringBuilder(ORDER_CODE_LENGTH);
        for (int i = 0; i < ORDER_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    @Override
    public List<Order> getOrderByProductIdAndUserId(Integer productId, Integer userId){
        return orderRepository.findByProductIdAndUserId(productId, userId);
    }
}
