package com.ey.service.impl;

import com.ey.entity.User;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.UserRepository;
import com.ey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User activateUser(Long userId) {
        User user = getUserById(userId);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public User deactivateUser(Long userId) {
        User user = getUserById(userId);
        user.setEnabled(false);
        return userRepository.save(user);
    }
}
