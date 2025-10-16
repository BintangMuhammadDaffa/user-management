package com.bionic.test.user_management.service;

import com.bionic.test.user_management.dto.ApiResponse;
import com.bionic.test.user_management.dto.LoginReq;
import com.bionic.test.user_management.dto.RegisterReq;
import com.bionic.test.user_management.entity.Token;
import com.bionic.test.user_management.entity.User;
import com.bionic.test.user_management.entity.UserProfile;
import com.bionic.test.user_management.repository.TokenRepository;
import com.bionic.test.user_management.repository.UserProfileRepo;
import com.bionic.test.user_management.repository.UserRepo;
import com.bionic.test.user_management.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepo userRepository;
    private final UserProfileRepo userProfileRepo;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepo userRepository,
                       UserProfileRepo profileRepository,
                       TokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userProfileRepo = profileRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public ApiResponse register(RegisterReq req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return new ApiResponse(false, "Email already registered");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.CUSTOMER)
                .status(User.Status.ACTIVE)
                .build();

        UserProfile profile = UserProfile.builder()
                .fullName(req.getFullName())
                .address(req.getAddress())
                .phone(req.getPhone())
                .user(user)
                .build();

        user.setProfile(profile);

        userRepository.save(user);
        return new ApiResponse(true, "User registered successfully");
    }

    @Transactional
    public ApiResponse login(LoginReq req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(user);

        tokenRepository.revokeAllUserTokens(user);

        Token tokenEntity = Token.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusDays(1)) // Assuming 1 day expiry
                .build();
        tokenRepository.save(tokenEntity);

        return new ApiResponse(true, "Login success", token);
    }

    @Transactional
    public ApiResponse logout() {
        org.springframework.security.core.Authentication authentication =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String email = userDetails.getUsername();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            tokenRepository.revokeAllUserTokens(user);
        }

        return new ApiResponse(true, "Logged out successfully");
    }

    @Transactional
    public ApiResponse refresh(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Revoke the old token
            Token oldToken = tokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Token not found"));
            oldToken.setRevoked(true);
            tokenRepository.save(oldToken);

            String newToken = jwtTokenProvider.generateToken(user);

            // Save the new token
            Token newTokenEntity = Token.builder()
                    .token(newToken)
                    .user(user)
                    .revoked(false)
                    .expiryDate(LocalDateTime.now().plusDays(1))
                    .build();
            tokenRepository.save(newTokenEntity);

            return new ApiResponse(true, "Token refreshed", newToken);
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
