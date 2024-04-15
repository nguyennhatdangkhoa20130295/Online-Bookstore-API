package vn.edu.hcmuaf.fit.websubject.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShow {
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String gender;
    private Date dateOfBirth;
    private String avatar;
    private int role;
    private Date createdAt;
    private Date updatedAt;
    private Boolean locked;
    private Boolean isSocial;
}
