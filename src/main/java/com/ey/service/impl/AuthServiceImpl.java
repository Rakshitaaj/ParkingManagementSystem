package com.ey.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ey.entity.Authority;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.exception.BadRequestException;
import com.ey.exception.ConflictException;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.AuthorityRepository;
import com.ey.repository.UserRepository;
import com.ey.security.JwtUtil;
import com.ey.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;   // ✅ MISSING PIECE (FIX)

    // ================= REGISTER =================

    @Override
    public User register(User user, String role) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        Authority authority = new Authority();
        authority.setRole(Role.valueOf(role));
        authority.setUser(savedUser);

        authorityRepository.save(authority);

        return savedUser;
    }

    // ================= LOGIN (JWT) =================

    @Override
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid username"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        // ✅ JWT generation
        return jwtUtil.generateToken(username);
    }

    // ================= FORGOT PASSWORD =================

    @Override
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email not registered"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);

        userRepository.save(user);

        // Email sending logic can be added later
        return resetToken;
    }

    // ================= RESET PASSWORD =================

    @Override
    public void resetPassword(String resetToken, String newPassword) {

        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() ->
                        new BadRequestException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        userRepository.save(user);
    }
}
