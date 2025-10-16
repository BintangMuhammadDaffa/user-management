package com.bionic.test.user_management.repository;

import com.bionic.test.user_management.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser_Email(String email);
}
