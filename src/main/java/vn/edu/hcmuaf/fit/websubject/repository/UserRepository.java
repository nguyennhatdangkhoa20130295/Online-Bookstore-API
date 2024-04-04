package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.websubject.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
  
    Optional<Users> findByUsername(String username);
  
    Optional<Users> findByEmail(String email);
  
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
