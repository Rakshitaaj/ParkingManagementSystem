package com.ey.mapper;

import com.ey.dto.request.RegisterRequestDTO;
import com.ey.dto.response.UserResponseDTO;
import com.ey.entity.User;

public class UserMapper {

    // RequestDTO → Entity
    public static User toEntity(RegisterRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    // Entity → ResponseDTO
    public static UserResponseDTO toResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}
