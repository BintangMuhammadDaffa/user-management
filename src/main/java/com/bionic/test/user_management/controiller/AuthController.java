package com.bionic.test.user_management.controiller;

import com.bionic.test.user_management.dto.ApiResponse;
import com.bionic.test.user_management.dto.LoginReq;
import com.bionic.test.user_management.dto.RegisterReq;
import com.bionic.test.user_management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterReq request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody LoginReq request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        return authService.logout();
    }

    @PostMapping("/refresh")
    public ApiResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
