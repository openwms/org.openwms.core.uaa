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

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A UserWrapperTest.
 * 
 * @author Heiko Scherrer
 */
class UserWrapperTest {

    private static final String TEST_USER = "TEST_USER";

    @Test void testUserWrapperForNull() {
        assertThatThrownBy(() -> new UserWrapper(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testUserWrapper() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.getUser()).isEqualTo(new User(TEST_USER));
    }

    @Test void testGetAuthoritiesWithNull() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.getAuthorities()).isNotNull();
    }

    @Test void testGetAuthorities() {
        var r = new Role("TEST_ROLE");
        r.addGrant(new Grant("TEST_GRANT"));
        var u = new User(TEST_USER);
        u.addRole(r);

        var uw = new UserWrapper(u);
        var auths = uw.getAuthorities();
        assertThat(auths).isNotEmpty();
        assertThat(auths.iterator().next().getAuthority()).isEqualTo("TEST_GRANT");
    }

    @Test void testGetPassword() throws Exception {
        var u = new User(TEST_USER);
        var enc = new BCryptPasswordEncoder(15);
        u.changePassword(enc.encode("PASS"), "PASS", enc);
        var uw = new UserWrapper(u);
        assertThat(uw.getPassword()).startsWith("$2a$15$");
    }

    @Test void testGetUsername() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.getUsername()).isEqualTo(TEST_USER);
    }

    @Test void testIsAccountNonExpired() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.isAccountNonExpired()).isTrue();
    }

    @Test void testIsAccountNonLocked() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.isAccountNonLocked()).isTrue();
    }

    @Test void testIsCredentialsNonExpired() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.isCredentialsNonExpired()).isTrue();
    }

    @Test void testIsEnabled() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw.isEnabled()).isTrue();
    }

    @Test void testHashCode() {
        var u = new User(TEST_USER);
        var u2 = new User("TEST_USER2");
        var u3 = new User(TEST_USER);
        var uw = new UserWrapper(u);
        var uw2 = new UserWrapper(u2);
        var uw3 = new UserWrapper(u3);

        var wrappers = new HashSet<UserWrapper>();
        wrappers.add(uw);
        wrappers.add(uw2);

        // Test for same return value
        assertThat(uw).doesNotHaveSameHashCodeAs(uw2)
                .hasSameHashCodeAs(uw3);

        assertThat(wrappers).contains(uw)
                .contains(uw2);
    }

    @Test void testEqualsObject() {
        var u = new User(TEST_USER);
        var usr = new User("TEST_USER2");
        var uw = new UserWrapper(u);
        var uw2 = new UserWrapper(u);
        var usrw = new UserWrapper(usr);

        // Test to itself
        assertThat(uw).isEqualTo(uw)
                .isNotEqualTo(null)
                .isEqualTo(uw2)
                .isNotEqualTo(TEST_USER)
                .isNotEqualTo(usrw);
        assertThat(uw2).isEqualTo(uw);
        assertThat(usrw.equals(uw)).isFalse();
    }

    @Test void testToString() {
        var uw = new UserWrapper(new User(TEST_USER));
        assertThat(uw).hasToString(TEST_USER);
    }
}
