package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.model.blog_category;

import java.util.Optional;

@Repository
public interface BlogCateRepository extends JpaRepository<blog_category, Integer> {
    Optional<blog_category> findById(int id);
}
