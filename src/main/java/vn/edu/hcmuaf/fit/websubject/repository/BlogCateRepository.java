package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;

import java.util.Optional;

@Repository
public interface BlogCateRepository extends JpaRepository<BlogCategory, Integer> {
    Optional<BlogCategory> findById(int id);
}
