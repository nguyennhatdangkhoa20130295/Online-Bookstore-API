package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.websubject.entity.CartItems;
import vn.edu.hcmuaf.fit.websubject.entity.Product;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    CartItems findByProductId(Integer idProduct);
    List<CartItems> findAllByUserId(Integer idUser);
}
