package com.ey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ey.entity.User;
import com.ey.repository.AuthorityRepository;
import com.ey.repository.UserRepository;
import com.ey.security.JwtUtil;
import com.ey.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock AuthorityRepository authorityRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;

    @InjectMocks AuthServiceImpl authService;

    @Test
    void login_success() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded");

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "encoded"))
                .thenReturn(true);
        when(jwtUtil.generateToken("admin"))
                .thenReturn("jwt-token");

        String token = authService.login("admin", "1234");

        assertEquals("jwt-token", token);
    }
}

