package com.ey.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.exception.ResourceNotFoundException;
import com.ey.repository.UserRepository;
import com.ey.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {

        logger.info("Fetch user by userId {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
                    logger.warn("User not found with id {}", userId);
                    return new ResourceNotFoundException("User not found with id " + userId);
                });
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetch all users");
        return userRepository.findAll();
    }
    
    

    @Override
    public User activateUser(Long userId) {

        logger.info("Activate user request for userId {}", userId);

        User user = getUserById(userId);
        user.setEnabled(true);
        User saved = userRepository.save(user);
        logger.info("User {} activated successfully", userId);
        return saved;
    }

    
    
    @Override
    public User deactivateUser(Long userId) {

        logger.info("Deactivate user request for userId {}", userId);

        User user = getUserById(userId);
        user.setEnabled(false);

        User saved = userRepository.save(user);

        logger.info("User {} deactivated successfully", userId);
        return saved;
    }
    
    @Override
    public List<User> getUsersByRole(Role role) {

        logger.info("Fetch users by role {}", role);
        return userRepository.findByAuthoritiesRole(role);
    }
    
    @Override
    public void deleteUser(Long userId) {

        logger.info("Delete (soft) user request for userId {}", userId);
        User user =userRepository.findById(userId).orElseThrow(() -> {
                            logger.warn("User not found for deletion: {}", userId);
                            return new ResourceNotFoundException("User not found");
                        });

        user.setEnabled(false);
        userRepository.save(user);
        logger.info("User {} soft-deleted successfully", userId);
    }
}
