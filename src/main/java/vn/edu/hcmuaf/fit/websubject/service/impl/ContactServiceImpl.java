package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Contact;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.ContactRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.ContactService;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Override
    public void sendContact(String fullName, String email, String title, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            Contact contact = new Contact();
            contact.setFullName(fullName);
            contact.setEmail(email);
            contact.setTitle(title);
            contact.setContent(content);
            contact.setUser(currentUser);
            contactRepository.save(contact);
        }
    }
}
