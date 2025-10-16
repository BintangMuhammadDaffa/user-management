package com.bionic.test.user_management.controiller;

import com.bionic.test.user_management.dto.StatusUpdateDTO;
import com.bionic.test.user_management.dto.UserResponseDTO;
import com.bionic.test.user_management.entity.User;
import com.bionic.test.user_management.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        Page<UserResponseDTO> users = adminUserService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = adminUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody UserResponseDTO userDTO) {
        adminUserService.updateUser(id, userDTO);
        Map<String, String> response = Map.of("message", "Profil update successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        Map<String, String> response = Map.of("message", "Profile deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(@PathVariable Long id, @RequestBody StatusUpdateDTO statusDTO) {
        User.Status status = User.Status.valueOf(statusDTO.getStatus().toUpperCase());
        adminUserService.updateUserStatus(id, status);
        Map<String, String> response = Map.of("message", "Status update successfully");
        return ResponseEntity.ok(response);
    }
}
