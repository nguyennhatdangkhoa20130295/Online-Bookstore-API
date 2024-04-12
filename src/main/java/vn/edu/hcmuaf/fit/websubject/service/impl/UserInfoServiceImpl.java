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
            if (userInfo.getFull_name() != null) {
                info.get().setFull_name(userInfo.getFull_name());
            }
            if (userInfo.getPhone_number() != null) {
                info.get().setPhone_number(userInfo.getPhone_number());
            }
            if (userInfo.getGender() != null) {
                info.get().setGender(userInfo.getGender());
            }
            if (userInfo.getDate_of_birth() != null) {
                info.get().setDate_of_birth(userInfo.getDate_of_birth());
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
