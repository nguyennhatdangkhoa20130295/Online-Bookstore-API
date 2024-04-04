package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);
    boolean checkIfUsernameExists(String username);

    boolean checkIfEmailExists(String email);
}
