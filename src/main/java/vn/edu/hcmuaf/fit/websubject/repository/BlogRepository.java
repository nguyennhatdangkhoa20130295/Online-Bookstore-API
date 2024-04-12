package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findAll(Specification<Blog> specification, Pageable pageable);
}
