package vn.edu.hcmuaf.fit.websubject.service;

public interface ContactService {
    void sendContact(String fullName, String email, String title, String content);
}
