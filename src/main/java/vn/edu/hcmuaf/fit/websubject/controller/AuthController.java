package vn.edu.hcmuaf.fit.websubject.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.websubject.jwt.JwtUtils;
import vn.edu.hcmuaf.fit.websubject.entity.*;
import vn.edu.hcmuaf.fit.websubject.model.*;
import vn.edu.hcmuaf.fit.websubject.payload.request.ForgotPassRequest;
import vn.edu.hcmuaf.fit.websubject.payload.request.LoginRequest;
import vn.edu.hcmuaf.fit.websubject.payload.request.SignupRequest;
import vn.edu.hcmuaf.fit.websubject.payload.response.JwtResponse;
import vn.edu.hcmuaf.fit.websubject.payload.response.MessageResponse;
import vn.edu.hcmuaf.fit.websubject.repository.RoleRepository;
import vn.edu.hcmuaf.fit.websubject.repository.TokenRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.impl.CustomUserDetailsImpl;
import vn.edu.hcmuaf.fit.websubject.security.CustomUserDetails;
import vn.edu.hcmuaf.fit.websubject.service.EmailService;
import vn.edu.hcmuaf.fit.websubject.service.OTPService;

import javax.mail.MessagingException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OTPService otpService;

    @Autowired
    EmailService emailService;

    private static final int OTP_LENGTH = 6;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        revokeAllUserToken(user);
        saveUserToken(user, jwt);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByDescription(EnumRole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByDescription(EnumRole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByDescription(EnumRole.MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByDescription(EnumRole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
//        var jwtToken = jwtUtils.generateJwtToken((Authentication) user);
//        revokeAllUserToken(saveUser);
//        saveUserToken(saveUser, jwtToken);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private void revokeAllUserToken(User user) {
      
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPassRequest forgotPassRequest) throws MessagingException {
        if(!userRepository.existsByEmail(forgotPassRequest.getEmail())){
            return ResponseEntity.badRequest().body("Không tìm thấy email.");
        }
        // Logic để gửi mã OTP đến email
        String otp = generateOTP();
        emailService.sendEmailForgot(forgotPassRequest.getEmail(),otp);
        otpService.saveOTP(forgotPassRequest.getEmail(), otp);
        // Gửi mã OTP đến email
        return ResponseEntity.ok("OTP "+ otp +" sent successfully.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ForgotPassRequest forgotPassRequest) {
        String savedOTP = otpService.getOTP(forgotPassRequest.getEmail());
        if (savedOTP != null && savedOTP.equals(forgotPassRequest.getOtp())) {
            // Xác thực thành công, thiết lập lại mật khẩu
            var user = userRepository.findByEmail(forgotPassRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (user != null) {
                user.setPassword(encoder.encode(forgotPassRequest.getNewPassword()));
                userRepository.save(user);
                // Xóa mã OTP sau khi đã sử dụng
                otpService.removeOTP(forgotPassRequest.getEmail());
                // Trả về thông báo thành công và mật khẩu mới cho người dùng
                return ResponseEntity.ok("Password reset successfully.");
            } else {
                return ResponseEntity.badRequest().body("User not found."); // Không tìm thấy người dùng
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    public String generateOTP() {
        // Dùng các ký tự từ 0-9 để tạo mã OTP
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        // Tạo mã OTP bằng cách chọn ngẫu nhiên các ký tự từ digits và thêm vào chuỗi OTP
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(digits.length());
            otp.append(digits.charAt(index));
        }

        return otp.toString();
    }

    private void revokeAllUserToken(Users user) {
        var validToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validToken.isEmpty())
            return;
        validToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
    public boolean hasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}