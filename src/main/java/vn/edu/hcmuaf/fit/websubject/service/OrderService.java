package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;
import vn.edu.hcmuaf.fit.websubject.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getUserOrders(Integer userId);
    Optional<OrderDetail> getOrderByProductIdAndUserId(Integer productId, Integer userId);
    void updateOrderStatus(Integer orderId, Integer newStatusId);
}
