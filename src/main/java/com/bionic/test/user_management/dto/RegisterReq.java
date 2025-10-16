package com.bionic.test.user_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String fullName;
    private String address;
    private String phone;
}
