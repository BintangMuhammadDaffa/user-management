package com.bionic.test.user_management.dto;

import com.bionic.test.user_management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private User.Role role;
    private User.Status status;
    private UserProfileDTO profile;
    private String password;
}
