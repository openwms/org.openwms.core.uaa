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

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A RoleTest.
 */
public class RoleTest {

    private static final String TEST_ROLE = "ROLE_TEST";
    private static final String TEST_ROLE2 = "ROLE_TEST2";
    private static final String TEST_DESCR = "ROLE Description";

    @Test void testCreation() {
        Role role = new Role(TEST_ROLE, TEST_DESCR);
        assertThat(role.getName()).isEqualTo(TEST_ROLE);
        assertEquals(TEST_DESCR, role.getDescription());
        Role role2 = new Role(TEST_ROLE2);
        assertEquals(TEST_ROLE2, role2.getName());
    }

    @Test void testCreationNegative1() {
        assertThrows(IllegalArgumentException.class, () -> new Role(""));
    }

    @Test void testCreationNegative2() {
        assertThrows(IllegalArgumentException.class, () -> new Role("", "TEST"));
    }

    @Test void testCreationNegative3() {
        assertThrows(IllegalArgumentException.class, () -> new Role(null));
    }

    @Test void testCreationNegative4() {
        assertThrows(IllegalArgumentException.class, () -> new Role(null, "TEST"));
    }

    @Test void testCreationNegative5() {
        new Role("TEST", null);
        new Role("TEST", "");
    }

    @Test void testAddUsers() {
        Role role = new Role(TEST_ROLE);
        User user = new User(TEST_ROLE);
        role.addUser(user);
        assertThat(role.getUsers()).hasSize(1);
        assertThat(role.getUsers()).contains(user);
    }

    @Test void testAddUsersNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.addUser(null));
    }

    @Test void testRemoveUser() {
        Role role = new Role(TEST_ROLE);
        User user = new User(TEST_ROLE);
        role.addUser(user);
        assertThat(role.getUsers()).hasSize(1);
        role.removeUser(user);
        assertThat(role.getUsers()).hasSize(0);
    }

    @Test void testRemoveUserNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.removeUser(null));
    }

    @Test void testSetUsers() {
        Role role = new Role(TEST_ROLE);
        User user = new User(TEST_ROLE);
        assertThat(role.getUsers()).hasSize(0);
        role.setUsers(new HashSet<>(Collections.singletonList(user)));
        assertThat(role.getUsers()).hasSize(1);
    }

    @Test void testSetUsersNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.setUsers(null));
    }

    @Test void testAddGrant() {
        Role role = new Role(TEST_ROLE);
        SecurityObject grant = new Grant(TEST_DESCR);
        assertThat(role.getGrants()).hasSize(0);
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
    }

    @Test void testAddGrantNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.addGrant(null));
    }

    @Test void testRemoveGrant() {
        Role role = new Role(TEST_ROLE);
        SecurityObject grant = new Grant(TEST_ROLE);
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
        role.removeGrant(grant);
        assertThat(role.getGrants()).hasSize(0);
    }

    @Test void testRemoveGrantNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.removeGrant(null));
    }

    @Test void testRemoveGrants() {
        Role role = new Role(TEST_ROLE);
        SecurityObject grant = new Grant(TEST_ROLE);
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
        role.removeGrants(Collections.singletonList(grant));
        assertThat(role.getGrants()).hasSize(0);
    }

    @Test void testRemoveGrantsNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.removeGrants(null));
    }

    @Test void testSetGrants() {
        Role role = new Role(TEST_ROLE);
        SecurityObject grant = new Grant(TEST_ROLE);
        assertThat(role.getGrants()).hasSize(0);
        role.setGrants(new HashSet<>(Collections.singletonList(grant)));
        assertThat(role.getGrants()).hasSize(1);
    }

    @Test void testSetGrantsNegative() {
        Role role = new Role(TEST_ROLE);
        assertThrows(IllegalArgumentException.class, () -> role.setGrants(null));
    }
}
