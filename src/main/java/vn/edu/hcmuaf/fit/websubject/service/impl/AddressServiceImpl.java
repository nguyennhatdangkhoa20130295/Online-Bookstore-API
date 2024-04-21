package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Address;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.AddressRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.AddressService;

import java.util.List;
import java.util.Optional;

import static vn.edu.hcmuaf.fit.websubject.payload.others.CurrentTime.getCurrentTimeInVietnam;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Address> getUserAddresses(Integer userId) {
        return addressRepository.findByUserIdOrderByIsDefaultDesc(userId);
    }

    @Override
    public Optional<Address> getAddressById(Integer id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address createAddress(Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        address.setUser(user);
        address.setCreatedAt(getCurrentTimeInVietnam());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Integer id, Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Optional<Address> existingAddressOptional = addressRepository.findById(id);
        if (existingAddressOptional.isEmpty()) {
            throw new RuntimeException("Address not found");
        }
        Address updatingAddress = existingAddressOptional.get();
        updatingAddress.setFullName(address.getFullName());
        updatingAddress.setPhoneNumber(address.getPhoneNumber());
        updatingAddress.setProvinceCity(address.getProvinceCity());
        updatingAddress.setCountyDistrict(address.getCountyDistrict());
        updatingAddress.setWardCommune(address.getWardCommune());
        updatingAddress.setHnumSname(address.getHnumSname());
        updatingAddress.setUpdatedAt(getCurrentTimeInVietnam());

        return addressRepository.save(updatingAddress);
    }

    @Override
    public void deleteAddress(Integer id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Optional<Address> getAddressDefaultByUserId(Integer id) {
        return addressRepository.findByAddressWithDefault(id);
    }

    @Override
    public void setDefaultAddress(Integer id) {
        addressRepository.setDefaultAddress(id);
        Optional<Address> address = getAddressById(id);
        if (address.isPresent()) {
            int user_id = address.get().getUser().getId();
            addressRepository.resetDefaultOtherAddress(user_id, id);
        }
    }

    @Override
    public void resetDefaultOtherAddress(int user_id, int selected_address_id) {
        addressRepository.resetDefaultOtherAddress(user_id, selected_address_id);
    }
}
