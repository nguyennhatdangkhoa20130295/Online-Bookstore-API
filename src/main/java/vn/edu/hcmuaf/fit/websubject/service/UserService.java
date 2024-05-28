package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.data.domain.Page;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.entity.UserShow;

import java.util.Date;
import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(int page, int perPage);

    UserShow getUserById(int idUser);

    void addUser(String username, String password, String email,
                 int role, String avatar, String fullName, String gender, Date dateOfBirth,
                 String phone, String locked, String isSocial);

    User editUser(int id, String email,
                  int role, String avatar, String fullName, String phone, String gender, Date dateOfBirth,
                  String locked, String isSocial);

    Page<User> findAllUsers(int page, int size, String sort, String order, String filter);

    Optional<User> getUserByUsername(String username);

    boolean checkIfUsernameExists(String username);

    boolean checkIfEmailExists(String email);

    void deleteUser(int idUser);
}
