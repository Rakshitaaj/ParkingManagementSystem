package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.User;

class UserTest {

    @Test
    void user_basic_properties_test() {

        User user = new User();
        user.setUserId(1L);
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setEnabled(true);
        user.setResetToken(null);

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("admin@gmail.com", user.getEmail());
        assertTrue(user.isEnabled());
        assertNull(user.getResetToken());
        assertNotEquals("user", user.getUsername());
    }
}
