package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.OrderStatus;
import vn.edu.hcmuaf.fit.websubject.entity.Orders;
import vn.edu.hcmuaf.fit.websubject.repository.OrderStatusRepository;
import vn.edu.hcmuaf.fit.websubject.repository.OrdersRepository;
import vn.edu.hcmuaf.fit.websubject.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    public boolean updateOrderStatus(Integer orderId, Integer newStatusId) {
        Optional<Orders> optionalOrder = ordersRepository.findById(orderId);
        Optional<OrderStatus> optionalNewStatus = orderStatusRepository.findById(newStatusId);
        if (optionalOrder.isPresent() && optionalNewStatus.isPresent()) {
            Orders order = optionalOrder.get();
            OrderStatus newStatus = optionalNewStatus.get();
            System.out.println(order);
            System.out.println(newStatus);

            order.setStatus(newStatus);
            ordersRepository.save(order);
            System.out.println(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Orders> getUserOrders(Integer userId) {
        return ordersRepository.findByUserId(userId);
    }
}
