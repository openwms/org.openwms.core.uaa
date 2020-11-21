/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.uaa.admin.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A UserWrapperTest.
 * 
 * @author Heiko Scherrer
 */
public class UserWrapperTest {

    private static final String TEST_USER = "TEST_USER";

    @Test void testUserWrapperForNull() {
        assertThrows(IllegalArgumentException.class, () -> new UserWrapper(null));
    }

    @Test void testUserWrapper() {
        UserWrapper uw = new UserWrapper(new User(TEST_USER));
        assertEquals(new User(TEST_USER), uw.getUser());
    }

    @Test void testGetAuthoritiesWithNull() {
        UserWrapper uw = new UserWrapper(new User(TEST_USER));
        assertNotNull(uw.getAuthorities());
    }

    @Test void testGetAuthorities() {
        Role r = new Role("TEST_ROLE");
        r.addGrant(new Grant("TEST_GRANT"));
        User u = new User(TEST_USER);
        u.addRole(r);

        UserWrapper uw = new UserWrapper(u);
        Collection<GrantedAuthority> auths = uw.getAuthorities();
        assertFalse(auths.isEmpty());
        assertEquals(auths.iterator().next().getAuthority(), "TEST_GRANT");
    }

    @Disabled
    @Test void testGetPassword() throws Exception {
        User u = new User(TEST_USER);
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder(15);
        u.changePassword(enc.encode("PASS"), "PASS", enc);
        UserWrapper uw = new UserWrapper(u);
        assertEquals("PASS", uw.getPassword());
    }

    @Test void testGetUsername() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertEquals(TEST_USER, uw.getUsername());
    }

    @Test void testIsAccountNonExpired() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertTrue(uw.isAccountNonExpired());
    }

    @Test void testIsAccountNonLocked() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertTrue(uw.isAccountNonLocked());
    }

    @Test void testIsCredentialsNonExpired() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertTrue(uw.isCredentialsNonExpired());
    }

    @Test void testIsEnabled() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertTrue(uw.isEnabled());
    }

    @Test void testEqualsObject() {
        User u = new User(TEST_USER);
        User usr = new User("TEST_USER2");
        UserWrapper uw = new UserWrapper(u);
        UserWrapper uw2 = new UserWrapper(u);
        UserWrapper usrw = new UserWrapper(usr);

        // Test to itself
        assertTrue(uw.equals(uw));
        // Test for null
        assertFalse(uw.equals(null));
        // Test for symmetric
        assertTrue(uw.equals(uw2));
        assertTrue(uw2.equals(uw));
        // Test incompatible types
        assertFalse(uw.equals(TEST_USER));
        assertFalse(uw.equals(usrw));
        assertFalse(usrw.equals(uw));
    }

    @Test void testHashCode() {
        User u = new User(TEST_USER);
        User u2 = new User("TEST_USER2");
        User u3 = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        UserWrapper uw2 = new UserWrapper(u2);
        UserWrapper uw3 = new UserWrapper(u3);

        Set<UserWrapper> wrappers = new HashSet<UserWrapper>();
        wrappers.add(uw);
        wrappers.add(uw2);

        // Test for same return value
        assertTrue(uw.hashCode() == uw.hashCode());
        // Test for same value for two refs
        assertTrue(uw.hashCode() == uw3.hashCode());

        assertTrue(wrappers.contains(uw));
        assertTrue(wrappers.contains(uw2));
    }

    @Test void testToString() {
        User u = new User(TEST_USER);
        UserWrapper uw = new UserWrapper(u);
        assertEquals(TEST_USER, uw.toString());
    }

}
