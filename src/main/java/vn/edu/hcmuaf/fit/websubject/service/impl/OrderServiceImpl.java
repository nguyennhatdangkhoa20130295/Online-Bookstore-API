package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Orders;
import vn.edu.hcmuaf.fit.websubject.repository.OrdersRepository;
import vn.edu.hcmuaf.fit.websubject.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    @Override
    public List<Orders> getUserOrders(Integer userId) {
        return ordersRepository.findByUserId(userId);
    }
}
