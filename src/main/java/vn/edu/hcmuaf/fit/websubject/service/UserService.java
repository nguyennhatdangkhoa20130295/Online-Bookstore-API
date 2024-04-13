package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.data.domain.Page;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.entity.User;

import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(int page, int perPage);
    Page<User> findAllUsers(int page, int size, String sort, String order, String filter);
    Optional<User> getUserByUsername(String username);
    boolean checkIfUsernameExists(String username);

    boolean checkIfEmailExists(String email);
}
