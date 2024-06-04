package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.payload.request.AddBlogRequest;
import vn.edu.hcmuaf.fit.websubject.payload.request.ContactRequest;
import vn.edu.hcmuaf.fit.websubject.service.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired
    ContactService contactService;

    @PostMapping("/send")
    public ResponseEntity<String> addBlog(@RequestBody ContactRequest contactRequest) {
        try {
            contactService.sendContact(contactRequest.getFullName(), contactRequest.getEmail(), contactRequest.getTitle(), contactRequest.getContent());
            return ResponseEntity.ok("Send contact successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add blog" + contactRequest);
        }
    }
}
