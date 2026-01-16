package com.ey.controller;

import com.ey.dto.request.RegisterRequestDTO;
import com.ey.dto.response.UserResponseDTO;
import com.ey.entity.User;
import com.ey.mapper.UserMapper;
import com.ey.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ================= REGISTER =================
    @PostMapping("/register/{role}")
    public ResponseEntity<UserResponseDTO> register(
            @PathVariable String role,
            @Valid @RequestBody RegisterRequestDTO request) {

        // DTO → Entity (via mapper)
        User user = UserMapper.toEntity(request);

        // Service call
        User savedUser = authService.register(user, role);

        // Entity → Response DTO (via mapper)
        UserResponseDTO response = UserMapper.toResponse(savedUser);

        return ResponseEntity.ok(response);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password) {

        return ResponseEntity.ok(authService.login(username, password));
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email) {

        String resetToken = authService.forgotPassword(email);
        return ResponseEntity.ok(
                "Reset token generated and sent to email: " + resetToken
        );
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String resetToken,
            @RequestParam String newPassword) {

        authService.resetPassword(resetToken, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }
}
