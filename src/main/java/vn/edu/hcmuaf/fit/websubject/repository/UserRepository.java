package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAll(Specification<User> specification, Pageable pageable);
    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);
  
    Optional<User> findByEmail(String email);
  
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
