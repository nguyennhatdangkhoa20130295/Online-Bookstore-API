package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Address;
import vn.edu.hcmuaf.fit.websubject.entity.Blog;
import vn.edu.hcmuaf.fit.websubject.entity.Category;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.entity.UserInfo;
import vn.edu.hcmuaf.fit.websubject.service.UserInfoService;
import vn.edu.hcmuaf.fit.websubject.service.impl.CustomUserDetailsImpl;
import vn.edu.hcmuaf.fit.websubject.service.AddressService;
import vn.edu.hcmuaf.fit.websubject.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AddressService addressService;

    @GetMapping("")
    public ResponseEntity<Page<User>> getAllBlogs(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "") String filter,
                                                  @RequestParam(defaultValue = "25") int perPage,
                                                  @RequestParam(defaultValue = "id") String sort,
                                                  @RequestParam(defaultValue = "DESC") String order) {
        Page<User> users = userService.findAllUsers(page, perPage, sort, order, filter);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/info")
    public ResponseEntity<?> getUserInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userService.getUserByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/info/{id}")
    public ResponseEntity<?> changeInformation(@PathVariable Integer id, @RequestBody UserInfo userInfo) {
        userInfoService.changeInformation(id, userInfo);
        return ResponseEntity.ok("Information changed successfully");
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getUserAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userService.getUserByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            List<Address> addresses = addressService.getUserAddresses(user.get().getId());
            return ResponseEntity.ok().body(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Integer id) {
        Optional<Address> addressOptional = addressService.getAddressById(id);
        if (addressOptional.isPresent()) {
            return ResponseEntity.ok().body(addressOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/addresses/default/{id}")
    public ResponseEntity<?> getAddressDefaultByUserId(@PathVariable Integer id) {
        Optional<Address> addressOptional = addressService.getAddressDefaultByUserId(id);
        if (addressOptional.isPresent()) {
            return ResponseEntity.ok().body(addressOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addresses")
    public ResponseEntity<?> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, address);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/addresses/default/{id}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Integer id) {
        addressService.setDefaultAddress(id);
        return ResponseEntity.ok("The user's address has been set by default");
    }
}
