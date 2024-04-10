package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
}