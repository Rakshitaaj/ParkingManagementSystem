package com.ey.service;

import com.ey.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    List<User> getAllUsers();

    User activateUser(Long userId);

    User deactivateUser(Long userId);
}
