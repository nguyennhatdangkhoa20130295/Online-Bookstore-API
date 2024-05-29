package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmuaf.fit.websubject.entity.Order;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    Optional<OrderDetail> findAllByOrderId(Integer orderId);
}
