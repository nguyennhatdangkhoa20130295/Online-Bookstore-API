package vn.edu.hcmuaf.fit.websubject.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    String email;
    int role;
    String avatar;
    String fullName;
    String phone;
    int locked;
    int isSocial;
}
