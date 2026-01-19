package com.ey;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ey.entity.Authority;
import com.ey.entity.User;
import com.ey.enums.Role;

class AuthorityTest {

    @Test
    void authority_basic_test() {

        User user = new User();
        user.setUserId(1L);

        Authority authority = new Authority();
        authority.setAuthorityId(10L);
        authority.setRole(Role.ROLE_ADMIN);
        authority.setUser(user);

        assertNotNull(authority);
        assertEquals(Role.ROLE_ADMIN, authority.getRole());
        assertNotNull(authority.getUser());
        assertEquals(1L, authority.getUser().getUserId());
        assertNotEquals(Role.ROLE_CUSTOMER, authority.getRole());
    }
}
