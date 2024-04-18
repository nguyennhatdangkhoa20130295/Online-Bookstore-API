package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.FavoriteProduct;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteProduct, Integer> {
    List<FavoriteProduct> findAllByUserId(Integer userId);
}
