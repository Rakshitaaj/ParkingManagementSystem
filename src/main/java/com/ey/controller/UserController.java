package com.ey.controller;

import com.ey.dto.response.UserResponseDTO;
import com.ey.entity.User;
import com.ey.mapper.UserMapper;
import com.ey.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ================= GET USER BY ID =================

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long userId) {

        User user = userService.getUserById(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }

    // ================= GET ALL USERS =================

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        List<UserResponseDTO> response =
                users.stream()
                        .map(UserMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================= ACTIVATE USER =================

    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponseDTO> activateUser(
            @PathVariable Long userId) {

        User user = userService.activateUser(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }

    // ================= DEACTIVATE USER =================

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivateUser(
            @PathVariable Long userId) {

        User user = userService.deactivateUser(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }
}
