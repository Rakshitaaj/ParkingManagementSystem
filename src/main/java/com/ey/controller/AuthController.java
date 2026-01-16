package com.ey.controller;

import com.ey.entity.User;
import com.ey.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    //REGISTER
    @PostMapping("/register/{role}")
    public ResponseEntity<?> register(
            @PathVariable String role,
            @RequestBody User user) {

        return ResponseEntity.ok(authService.register(user, role));
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
