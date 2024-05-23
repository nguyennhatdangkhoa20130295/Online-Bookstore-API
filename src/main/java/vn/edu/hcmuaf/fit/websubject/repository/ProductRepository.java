package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.Category;
import vn.edu.hcmuaf.fit.websubject.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor {
//    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId OR p.category.parentCategory.id = :categoryId OR p.category.parentCategory.parentCategory.id = :categoryId")
//    Page<Product> findByCategoryParentOrCategory(Integer categoryId, Specification<Product> specification, Pageable pageable);

    List<Product> findTop3ByOrderByIdDesc();

    Optional<Product> findById(Integer id);

//    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

}
