package com.ey.service;

import java.util.List;

import com.ey.entity.User;
import com.ey.enums.Role;

public interface UserService {

    User getUserById(Long userId);

    List<User> getAllUsers();

    User activateUser(Long userId);

    User deactivateUser(Long userId);   
    List<User> getUsersByRole(Role role);
    void deleteUser(Long userId);


}
