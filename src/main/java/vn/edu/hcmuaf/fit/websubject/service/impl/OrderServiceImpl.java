package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.*;
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
    InventoryRepository inventoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ORDER_CODE_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();

    @Override
    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUserIdOrderByIdDesc(userId);
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
        Optional<Promotion> promotionOptional = promotionRepository.findById(order.getPromotion().getId());
        if (promotionOptional.isEmpty()) {
            order.setPromotion(null);
        } else {
            Promotion promotion = promotionOptional.get();
            order.setPromotion(promotion);
        }
        order.setOrderCode(generateOrderCode());
        order.setOrderDate(CurrentTime.getCurrentTimeInVietnam());
        if (order.getPaymentMethod().equals("cashondelivery")) {
            order.setPaymentMethod("Thanh toán khi nhận hàng");
        }
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByPromoCode(String code, Integer userId) {
        List<Order> orders = orderRepository.findByPromoCode(code, userId);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public void createOrderDetail(OrderDetail orderDetail) {
        updateInventory(orderDetail.getProduct().getId(), orderDetail.getQuantity());
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

    private void updateInventory(int productId, int quantity) {
        Product existingProduct = productRepository.findById(productId).get();
        Inventory inventory = inventoryRepository.findByProductIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        if (inventory.getRemainingQuantity() < quantity) {
            throw new RuntimeException("Không đủ hàng cho sản phẩm: " + existingProduct.getTitle());
        }
        inventory.setRemainingQuantity(inventory.getRemainingQuantity() - quantity);
        inventory.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
        if (inventory.getRemainingQuantity() == 0) {
            inventory.setActive(false);
        }
        inventoryRepository.save(inventory);
    }

    @Override
    public Optional<Order> getOrder(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> getOrderByProductIdAndUserId(Integer productId, Integer userId) {
        return orderRepository.findByProductIdAndUserId(productId, userId);
    }
}
