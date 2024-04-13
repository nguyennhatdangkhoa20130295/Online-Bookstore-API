package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmuaf.fit.websubject.entity.OrderDetail;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("""
        select o from OrderDetail o inner join Product p on o.product.id = p.id
        where p.id = :productId
    """)
    Optional<OrderDetail> findByProductId(Integer productId);
}
