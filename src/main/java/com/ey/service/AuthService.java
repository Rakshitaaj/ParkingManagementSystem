package com.ey.service;

import com.ey.entity.User;

public interface AuthService {

    User register(User user, String role);

    String login(String username, String password);

    String forgotPassword(String email);

    void resetPassword(String resetToken, String newPassword);
}
