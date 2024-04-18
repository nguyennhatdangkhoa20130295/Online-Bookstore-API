package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.entity.UserInfo;
import vn.edu.hcmuaf.fit.websubject.repository.UserInfoRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.UserInfoService;

import java.util.Optional;

import static vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime.getCurrentTimeInVietnam;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<UserInfo> getUserInfo(Integer userId) {
        return userInfoRepository.findByUserId(userId);
    }

    @Override
    public UserInfo changeInformation(Integer id, UserInfo userInfo) {
        Optional<UserInfo> infoOptional = userInfoRepository.findById(id);
        if (infoOptional.isEmpty()) {
            throw new RuntimeException("User info not found");
        }
        UserInfo currentInfo = infoOptional.get();
        if (userInfo.getFullName() != null) {
            currentInfo.setFullName(userInfo.getFullName());
        }
        if (userInfo.getPhoneNumber() != null) {
            currentInfo.setPhoneNumber(userInfo.getPhoneNumber());
        }
        if (userInfo.getGender() != null) {
            currentInfo.setGender(userInfo.getGender());
        }
        if (userInfo.getDateOfBirth() != null) {
            currentInfo.setDateOfBirth(userInfo.getDateOfBirth());
        }
        if (userInfo.getAvatar() != null) {
            currentInfo.setAvatar(userInfo.getAvatar());
        }
        currentInfo.setUpdatedAt(getCurrentTimeInVietnam());
        return userInfoRepository.save(currentInfo);
    }

    @Override
    public UserInfo createInformation(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        userInfo.setUser(user);
        userInfo.setCreatedAt(getCurrentTimeInVietnam());
        return userInfoRepository.save(userInfo);
    }
}
