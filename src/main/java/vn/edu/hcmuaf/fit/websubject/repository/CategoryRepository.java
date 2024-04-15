package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentIdAndActiveTrue(Integer parentId);

    List<Category> findByParentIdIsNullAndActiveTrue();

    Page<Category> findAll(Specification<Category> specification, Pageable pageable);

    boolean existsByNameAndParentId(String name, Integer parentId);


}
