package com.bionic.test.user_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
