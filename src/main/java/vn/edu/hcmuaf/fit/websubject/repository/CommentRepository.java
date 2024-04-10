package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.websubject.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByProductId(Integer id);
    List<Comment> findAllByUserId(Integer id);
    Optional<Comment> findById(Integer id);
}
