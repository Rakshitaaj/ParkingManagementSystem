package com.ey.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger =LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User register(User user, String role) {
        logger.info("Register attempt for username: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Registration failed - username already exists: {}",user.getUsername());
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed - email already exists: {}",user.getEmail());
            throw new ConflictException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        Authority authority = new Authority();
        authority.setRole(Role.valueOf(role));
        authority.setUser(savedUser);
        authorityRepository.save(authority);
        logger.info("User registered successfully with role {} and userId {}",role, savedUser.getUserId());
        return savedUser;
    }

    @Override
    public String login(String username, String password) {

        logger.info("Login attempt for username: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
                    logger.error("Login failed - invalid username: {}", username);
                    return new ResourceNotFoundException("Invalid username");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Login failed - invalid password for username: {}", username);
            throw new BadRequestException("Invalid password");
        }
        String token = jwtUtil.generateToken(username);
        logger.info("Login successful for username: {}", username);
        return token;
    }

    @Override
    public String forgotPassword(String email) {

        logger.info("Forgot password request for email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
                    logger.warn("Forgot password failed - email not registered: {}", email);
                    return new ResourceNotFoundException("Email not registered");
                });

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);
        logger.info("Password reset token generated for userId {}",user.getUserId());
        return resetToken;
    }


    @Override
    public void resetPassword(String resetToken, String newPassword) {

        logger.info("Reset password attempt with token: {}", resetToken);

        User user = userRepository.findByResetToken(resetToken).orElseThrow(() -> {
                    logger.error("Reset password failed - invalid token");
                    return new BadRequestException("Invalid reset token");
                });

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        logger.info("Password reset successful for userId {}",user.getUserId());
    }
}
