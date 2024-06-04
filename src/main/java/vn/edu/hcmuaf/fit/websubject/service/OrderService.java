package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Order;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getUserOrders(Integer userId);

    Order getLatestOrder(Integer userId);

    Order createOrder(Order order);

    Order getOrderByPromoCode(String code, Integer userId);

    void createOrderDetail(OrderDetail orderDetail);

    String generateOrderCode();

    Optional<Order> getOrder(Integer orderId);

    List<Order> getOrderByProductIdAndUserId(Integer productId, Integer userId);
}
