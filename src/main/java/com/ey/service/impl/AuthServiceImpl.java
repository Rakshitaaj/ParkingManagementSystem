package com.ey.service.impl;

import com.ey.entity.Authority;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.repository.AuthorityRepository;
import com.ey.repository.UserRepository;
import com.ey.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user, String role) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        Authority authority = new Authority();
        authority.setRole(Role.valueOf(role));
        authority.setUser(savedUser);

        authorityRepository.save(authority);

        return savedUser;
    }

    @Override
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // JWT token will be generated later in security layer
        return "LOGIN_SUCCESS";
    }

    @Override
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);

        userRepository.save(user);

        // Email sending logic can be added later
        return resetToken;
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {

        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);

        userRepository.save(user);
    }
}
