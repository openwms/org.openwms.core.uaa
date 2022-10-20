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
package org.openwms.core.uaa.impl;

import org.junit.jupiter.api.Test;
import org.openwms.core.uaa.impl.SystemUser;
import org.openwms.core.uaa.impl.SystemUserWrapper;
import org.openwms.core.uaa.impl.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A SystemUserWrapperTest.
 * 
 * @author Heiko Scherrer
 */
class SystemUserWrapperTest {

    private static final String TEST_USER = "TEST_USER";

    @Test void testAddDefaultGrants() {
        var suw = new SystemUserWrapper(new User(TEST_USER));
        assertThat(suw.getAuthorities()).hasSize(1);
        assertThat(suw.getAuthorities().iterator().next().getAuthority()).isEqualTo(SystemUser.SYSTEM_ROLE_NAME);
    }

    @Test void testGetPassword() throws Exception {
        var user = new User(TEST_USER);
        var enc = new BCryptPasswordEncoder(15);
        user.changePassword(enc.encode("PASS"), "PASS", enc);
        var suw = new SystemUserWrapper(user);
        assertThat(suw.getPassword()).startsWith("$2a$15$");
    }

    @Test void testSystemUserWrapper() {
        assertThatThrownBy(() -> new SystemUserWrapper(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testHashCode() {
        var u = new User(TEST_USER);
        var u2 = new User("TEST_USER2");
        var u3 = new User(TEST_USER);
        var uw = new SystemUserWrapper(u);
        var uw2 = new SystemUserWrapper(u2);
        var uw3 = new SystemUserWrapper(u3);

        var wrappers = new HashSet<>();
        wrappers.add(uw);
        wrappers.add(uw2);

        // Test for same return value
        assertThat(uw).doesNotHaveSameHashCodeAs(uw2)
                .hasSameHashCodeAs(uw3);

        assertThat(wrappers).contains(uw, uw2);
    }

    @Test void testEqualsObject() {
        var u = new User(TEST_USER);
        var usr = new User("TEST_USER2");
        var suw = new SystemUserWrapper(u);
        var suw2 = new SystemUserWrapper(u);
        var susrw = new SystemUserWrapper(usr);

        // Test to itself
        assertThat(suw).isEqualTo(suw)
                .isNotEqualTo(null)
                .isEqualTo(suw2)
                .isNotEqualTo(TEST_USER)
                .isNotEqualTo(susrw);
        assertThat(suw2).isEqualTo(suw);
        assertThat(susrw).isNotEqualTo(suw);

        // This password is null, the other is set
        suw2.setPassword("PASS");
        assertThat(suw).isNotEqualTo(suw2);

        // Same password, but different user is wrapped
        susrw.setPassword("PASS");
        suw.setPassword("PASS");
        assertThat(suw).isNotEqualTo(susrw);

        // Same user and same password
        suw.setPassword("PASS");
        assertThat(suw).isEqualTo(suw2);

        // Same user but different password
        suw2.setPassword("PASS2");
        assertThat(suw).isNotEqualTo(suw2);
    }
}