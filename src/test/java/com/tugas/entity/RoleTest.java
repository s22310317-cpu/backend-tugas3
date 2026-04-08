package com.tugas.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role("ROLE_USER");
    }

    @Test
    void testRoleCreationWithConstructor() {
        assertNotNull(role);
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testRoleSettersAndGetters() {
        role.setId(1L);
        role.setName("ROLE_ADMIN");
        
        assertEquals(1L, role.getId());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testRoleCreationEmpty() {
        Role emptyRole = new Role();
        assertNotNull(emptyRole);
        assertNull(emptyRole.getName());
    }

    @Test
    void testRoleWithDifferentNames() {
        String[] roleNames = { "ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_GUEST" };
        
        for (String roleName : roleNames) {
            role.setName(roleName);
            assertEquals(roleName, role.getName());
        }
    }

    @Test
    void testRoleFullConstructor() {
        Role roleWithId = new Role(5L, "ROLE_SUPERADMIN");
        assertEquals(5L, roleWithId.getId());
        assertEquals("ROLE_SUPERADMIN", roleWithId.getName());
    }
}
