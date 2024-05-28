package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor {

    List<Product> findTop3ByOrderByIdDesc();

    Optional<Product> findById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId OR p.category.parentCategory.id = :categoryId OR p.category.parentCategory.parentCategory.id = :categoryId")
    List<Product> findByCategoryId(Integer categoryId);

    @Query(value = "SELECT * FROM Product ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Product> findRandomProducts();

    @Query("SELECT p FROM Product p LEFT JOIN p.comments c GROUP BY p.id ORDER BY COUNT(c) DESC LIMIT 2")
    List<Product> findTopReviewProducts();
}
