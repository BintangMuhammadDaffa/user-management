package com.bionic.test.user_management.service;

import com.bionic.test.user_management.dto.UserProfileDTO;
import com.bionic.test.user_management.entity.User;
import com.bionic.test.user_management.entity.UserProfile;
import com.bionic.test.user_management.exception.ResourceNotFoundException;
import com.bionic.test.user_management.repository.UserProfileRepo;
import com.bionic.test.user_management.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);
    private final UserProfileRepo userProfileRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public UserProfileDTO getMyProfile() {
        User currentUser = getCurrentUser();
        UserProfile profile = userProfileRepo.findById(currentUser.getProfile().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return modelMapper.map(profile, UserProfileDTO.class);
    }

    public UserProfileDTO updateMyProfile(UserProfileDTO profileDTO) {
        User currentUser = getCurrentUser();
        UserProfile profile = currentUser.getProfile();
        if (profile == null) {
            throw new ResourceNotFoundException("Profile not found");
        }

        if (profileDTO.getFullName() != null){
            profile.setFullName(profileDTO.getFullName());
        }
        if (profileDTO.getAddress() != null){
            profile.setAddress(profileDTO.getAddress());
        }
        if (profileDTO.getPhone() != null){
            profile.setPhone(profileDTO.getPhone());
        }

        userProfileRepo.save(profile);
        return modelMapper.map(profile, UserProfileDTO.class);
    }

    @Transactional
    public void deleteMyProfile() {
        User currentUser = getCurrentUser();
        logger.info("Mencoba menghapus akun untuk user: {}", currentUser.getEmail());

        userRepo.delete(currentUser);
        logger.info("Akun user berhasil dihapus.");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userProfileRepo.findByUser_Email(email)
                .map(UserProfile::getUser)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
