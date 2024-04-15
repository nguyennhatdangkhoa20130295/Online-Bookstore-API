package vn.edu.hcmuaf.fit.websubject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.entity.UserInfo;
import vn.edu.hcmuaf.fit.websubject.entity.*;
import vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime;
import vn.edu.hcmuaf.fit.websubject.repository.RoleRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public Page<User> getAllUsers(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUserById(int idUser) {
        return userRepository.findById(idUser);
    }

    public Page<User> findAllUsers(int page, int size, String sort, String order, String filter) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sortPa = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sortPa);

        JsonNode jsonFilter;
        try {
            jsonFilter = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (jsonFilter.has("q")) {
                String searchStr = jsonFilter.get("q").asText();
                predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + searchStr.toLowerCase() + "%");
            }
            return predicate;
        };

        return userRepository.findAll(specification, pageable);
    }

    @Override
    public void addUser(String username, String password, String email,
                        int role, String avatar, String fullName, String phone,
                        String locked, String isSocial) {
        if (userRepository.existsByUsername(username)) {
            System.out.println("Username is already taken!");
        } else if (userRepository.existsByEmail(email)) {
            System.out.println("Email is already in use!");
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setEmail(email);
            user.getUserInfo().setFullName(fullName);
            user.getUserInfo().setPhoneNumber(phone);
            Set<Role> roles = new HashSet<>();
            switch (role) {
                case 1:
                    Role adminRole = roleRepository.findByDescription(EnumRole.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case 2:
                    Role modRole = roleRepository.findByDescription(EnumRole.MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                case 3:
                    Role userRole = roleRepository.findByDescription(EnumRole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);

                    break;
                default:
                    Role userR = roleRepository.findByDescription(EnumRole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userR);
            }

            user.setRoles(roles);
            user.getUserInfo().setAvatar(avatar);
            user.setCreatedAt(CurrentTime.getCurrentTimeInVietnam());
            user.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
            if (locked.equals("false"))
                user.setLocked(false);
            else
                user.setLocked(true);
            if (isSocial.equals("false"))
                user.setIsSocial(false);
            else
                user.setIsSocial(true);
            userRepository.save(user);
        }
    }

    @Override
    public User editUser(int id, String email,
                         int role, String avatar, String fullName, String phone,
                         String locked, String isSocial) {
        User newInforUser = null;
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            newInforUser = userOptional.get();
            newInforUser.setEmail(email);
            newInforUser.getUserInfo().setFullName(fullName);
            newInforUser.getUserInfo().setPhoneNumber(phone);
            newInforUser.getRoles().clear();
            Set<Role> roles = new HashSet<>();
            switch (role) {
                case 1:
                    Role adminRole = roleRepository.findByDescription(EnumRole.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case 2:
                    Role modRole = roleRepository.findByDescription(EnumRole.MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                case 3:
                    Role userRole = roleRepository.findByDescription(EnumRole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);

                    break;
                default:
                    Role userR = roleRepository.findByDescription(EnumRole.USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userR);
            }

            newInforUser.setRoles(roles);
            newInforUser.getUserInfo().setAvatar(avatar);
            newInforUser.setUpdatedAt(CurrentTime.getCurrentTimeInVietnam());
            if (locked.equals("false"))
                newInforUser.setLocked(false);
            else
                newInforUser.setLocked(true);
            if (isSocial.equals("false"))
                newInforUser.setIsSocial(false);
            else
                newInforUser.setIsSocial(true);
            userRepository.save(newInforUser);
        }
        return newInforUser;
    }

    @Override
    public void deleteUser(int idUser) {
        userRepository.deleteById(idUser);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
