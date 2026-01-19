package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.UserResponseDTO;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.mapper.UserMapper;
import com.ey.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long userId) {

        User user = userService.getUserById(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        List<UserResponseDTO> response =
                users.stream()
                        .map(UserMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponseDTO> activateUser(
            @PathVariable Long userId) {

        User user = userService.activateUser(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivateUser(
            @PathVariable Long userId) {

        User user = userService.deactivateUser(userId);

        return ResponseEntity.ok(
                UserMapper.toResponse(user));
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @PathVariable Role role) {

        List<UserResponseDTO> response =
                userService.getUsersByRole(role)
                        .stream()
                        .map(UserMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        userService.deleteUser(userId);
        return ResponseEntity.ok("User deactivated successfully");
    }


}
