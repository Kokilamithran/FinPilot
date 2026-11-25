package com.finpilot.auth;

import com.finpilot.dto.request.LoginRequest;
import com.finpilot.dto.request.RegisterRequest;
import com.finpilot.dto.response.LoginResponse;
import com.finpilot.dto.response.RegisterResponse;
import com.finpilot.dto.response.UserResponse;
import com.finpilot.entity.User;
import com.finpilot.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final SecurityUtils securityUtils;

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        User currentUser = securityUtils.getCurrentUser();
        return authenticationService.getCurrentUserResponse(currentUser);
    }
}