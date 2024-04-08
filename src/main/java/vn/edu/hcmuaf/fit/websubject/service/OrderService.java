package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> getUserOrders(Integer userId);
}
