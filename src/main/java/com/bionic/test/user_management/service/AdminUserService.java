package com.bionic.test.user_management.service;

import com.bionic.test.user_management.dto.UserResponseDTO;
import com.bionic.test.user_management.entity.User;
import com.bionic.test.user_management.entity.UserProfile;
import com.bionic.test.user_management.exception.ResourceNotFoundException;
import com.bionic.test.user_management.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepo.findAllWithProfiles(pageable);
        return users.map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepo.findByIdWithProfile(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO updateUser(Long id, UserResponseDTO userDTO) {
        User user = userRepo.findByIdWithProfile(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getRole() != null){
            user.setRole(userDTO.getRole());
        }

        if (userDTO.getStatus() != null){
            user.setStatus(userDTO.getStatus());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setPassword(userDTO.getPassword());
        }

        if (userDTO.getProfile() != null) {
            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            if (userDTO.getProfile().getFullName() != null) {
                profile.setFullName(userDTO.getProfile().getFullName());
            }
            if (userDTO.getProfile().getAddress() != null) {
                profile.setAddress(userDTO.getProfile().getAddress());
            }
            if (userDTO.getProfile().getPhone() != null) {
                profile.setPhone(userDTO.getProfile().getPhone());
            }
        }

        userRepo.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user);
    }

    public UserResponseDTO updateUserStatus(Long id, User.Status status) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(status);
        userRepo.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
