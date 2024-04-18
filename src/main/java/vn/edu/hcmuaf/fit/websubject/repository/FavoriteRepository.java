package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.FavoriteProduct;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.User;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteProduct, Integer> {
    List<FavoriteProduct> findAllByUserId(Integer userId);

    FavoriteProduct findByProductId(Integer productId);
}
