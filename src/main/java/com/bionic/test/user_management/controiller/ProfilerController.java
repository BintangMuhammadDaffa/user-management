package com.bionic.test.user_management.controiller;

import com.bionic.test.user_management.dto.UserProfileDTO;
import com.bionic.test.user_management.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfilerController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        UserProfileDTO profile = userProfileService.getMyProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> updateMyProfile(@RequestBody UserProfileDTO profileDTO) {
        userProfileService.updateMyProfile(profileDTO);
        Map<String, String> response = Map.of("message", "Profil update successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity <Map<String, String>> deleteMyProfile() {
        userProfileService.deleteMyProfile();
        Map<String, String> response = Map.of("message", "Profile deleted successfully");
        return ResponseEntity.ok(response);
    }
}
