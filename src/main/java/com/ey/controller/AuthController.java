package com.ey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.RegisterRequestDTO;
import com.ey.dto.response.UserResponseDTO;
import com.ey.entity.User;
import com.ey.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    //REGISTER
    @PostMapping("/register/{role}")
    public ResponseEntity<UserResponseDTO> register(
            @PathVariable String role,
            @Valid @RequestBody RegisterRequestDTO request) {

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = authService.register(user, role);

        UserResponseDTO response = new UserResponseDTO();
        response.setUserId(savedUser.getUserId());
        response.setFullName(savedUser.getFullName());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setEnabled(savedUser.isEnabled());

        return ResponseEntity.ok(response);
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        return ResponseEntity.ok(authService.login(username, password));
    }

    //FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        String resetToken = authService.forgotPassword(email);
        return ResponseEntity.ok(
                "Reset token generated and sent to email: " + resetToken
        );
    }

    //RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String resetToken,
            @RequestParam String newPassword) {

        authService.resetPassword(resetToken, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }
}
