package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("""
        select o from OrderDetail od inner join Product p on od.product.id = p.id inner join Order o on o.id = od.order.id
        where p.id = :productId and od.order.user.id = :userId and o.status.id = 5
    """)
    Optional<OrderDetail> findByProductIdAndUserId(Integer productId, Integer userId);
}
