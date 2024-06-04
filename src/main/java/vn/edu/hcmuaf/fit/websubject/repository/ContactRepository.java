package vn.edu.hcmuaf.fit.websubject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.edu.hcmuaf.fit.websubject.entity.Contact;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Integer>, JpaSpecificationExecutor {
    Optional<Contact> findById(Integer id);
}
