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
import org.openwms.core.exception.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An UserTest.
 *
 * @author Heiko Scherrer
 */
public class UserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTest.class);
    private static final String TEST_USER1 = "Test username1";
    private static final String TEST_USER2 = "Test username2";
    private static final String TEST_PASSWORD = "Test password";

    @Test void testCreation() {
        User user1 = new User(TEST_USER1);
        assertThat(TEST_USER1).isEqualTo(user1.getUsername());
        assertThat(user1.getPk()).isNull();
        assertThat(user1.isNew()).isTrue();
    }

    @Test void testCreation2() {
        User user1 = new User(TEST_USER2, TEST_PASSWORD);
        assertThat(TEST_USER2).isEqualTo(user1.getUsername());
        assertThat(TEST_PASSWORD).isEqualTo(user1.getPassword());
        assertThat(user1.getPk()).isNull();
        assertThat(user1.isNew()).isTrue();
    }

    @Test void testCreationNegative() {
        assertThrows(IllegalArgumentException.class, () -> new User(""));
    }

    @Test void testCreationNegative2() {
        assertThrows(IllegalArgumentException.class, () -> new User("", TEST_PASSWORD));
    }

    @Test void testCreationNegativ3() {
        assertThrows(IllegalArgumentException.class, () -> new User(null));
    }

    @Test void testCreationNegative4() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, TEST_PASSWORD));
    }

    @Disabled
    @Test void testPasswordHistory() {
        User u1 = new User(TEST_USER1);
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder(15);

        for (int i = 0; i <= User.NUMBER_STORED_PASSWORDS + 5; i++) {
            try {
                if (i <= User.NUMBER_STORED_PASSWORDS) {
                    u1.changePassword(enc.encode(String.valueOf(i)), String.valueOf(i), enc);
                } else {
                    LOGGER.debug("Number of password history exceeded, resetting to:0");
                    u1.changePassword(enc.encode("0"), "0", enc);
                }
            } catch (InvalidPasswordException e) {
                if (i <= User.NUMBER_STORED_PASSWORDS) {
                    fail("Number of acceptable passwords not exceeded");
                } else {
                    LOGGER.debug("OK: Exception because password is already in the list, set password to:" + i);
                    try {
                        u1.changePassword(enc.encode(String.valueOf(i)), String.valueOf(i), enc);
                    } catch (InvalidPasswordException ex) {
                        LOGGER.debug("Error" + ex.getMessage());
                    }
                }
            }
            try {
                // Just wait to setup changeDate correctly. Usually password
                // changes aren't done within the same millisecond
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.debug("Error" + e.getMessage());
            }
        }
        // Verify that the password list was sorted in the correct order.
        String oldPassword = null;
        for (UserPassword pw : u1.getPasswords()) {
            if (oldPassword == null) {
                oldPassword = pw.getPassword();
                continue;
            }
            assertThat(Integer.valueOf(oldPassword)).isGreaterThan(Integer.valueOf(pw.getPassword()));
        }
    }

    @Test void testHashCodeEquals() {
        User user1 = new User(TEST_USER1);
        User user2 = new User(TEST_USER1);
        User user3 = new User(TEST_USER2);

        // Just the name is considered
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        // Test behavior in hashed collections
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        assertThat(users).hasSize(1);
        users.add(user3);
        assertThat(users).hasSize(2);
    }
}