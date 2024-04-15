package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.UserInfo;
import vn.edu.hcmuaf.fit.websubject.repository.UserInfoRepository;
import vn.edu.hcmuaf.fit.websubject.service.UserInfoService;

import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Optional<UserInfo> getUserInfo(Integer userId) {
        return userInfoRepository.findByUserId(userId);
    }

    @Override
    public UserInfo changeInformation(Integer id, UserInfo userInfo) {
        Optional<UserInfo> info = userInfoRepository.findById(id);
        if (info.isPresent()) {
            if (userInfo.getFullName() != null) {
                info.get().setFullName(userInfo.getFullName());
            }
            if (userInfo.getPhoneNumber() != null) {
                info.get().setPhoneNumber(userInfo.getPhoneNumber());
            }
            if (userInfo.getGender() != null) {
                info.get().setGender(userInfo.getGender());
            }
            if (userInfo.getDateOfBirth() != null) {
                info.get().setDateOfBirth(userInfo.getDateOfBirth());
            }
            if (userInfo.getAvatar() != null) {
                info.get().setAvatar(userInfo.getAvatar());
            }
            return userInfoRepository.save(info.get());
        } else {
            return null;
        }
    }
}
