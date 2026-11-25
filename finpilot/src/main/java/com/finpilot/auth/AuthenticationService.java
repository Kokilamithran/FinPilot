package com.finpilot.auth;

import com.finpilot.dto.request.LoginRequest;
import com.finpilot.dto.request.RegisterRequest;
import com.finpilot.dto.response.LoginResponse;
import com.finpilot.dto.response.RegisterResponse;
import com.finpilot.entity.Role;
import com.finpilot.entity.User;
import com.finpilot.exception.DuplicateResourceException;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.RoleRepository;
import com.finpilot.repository.UserRepository;
import com.finpilot.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("USER role not found"));

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(role);

        userRepository.save(user);

        return new RegisterResponse("User Registered Successfully");
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getEmail());

        return new LoginResponse(
                token,
                "Login Successful"
        );
    }

    public com.finpilot.dto.response.UserResponse getCurrentUserResponse(User user) {
        return com.finpilot.dto.response.UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole() != null ? user.getRole().getRoleName() : "USER")
                .currencyPreference(user.getCurrencyPreference())
                .build();
    }

}