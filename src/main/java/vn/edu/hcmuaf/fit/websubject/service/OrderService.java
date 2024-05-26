package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Order;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;

import java.util.List;

public interface OrderService {
    List<Order> getUserOrders(Integer userId);
    Order getLatestOrder(Integer userId);
    Order createOrder(Order order);
    void createOrderDetail(OrderDetail orderDetail);
    String generateOrderCode();
}
