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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A RoleTest.
 */
class RoleTest {

    private static final String TEST_ROLE = "ROLE_TEST";
    private static final String TEST_ROLE2 = "ROLE_TEST2";
    private static final String TEST_DESCR = "ROLE Description";

    @Test void testCreation() {
        var role = new Role(TEST_ROLE, TEST_DESCR);
        assertThat(role.getName()).isEqualTo(TEST_ROLE);
        assertThat(role.getDescription()).isEqualTo(TEST_DESCR);

        var role2 = new Role(TEST_ROLE);
        assertThat(role2.getName()).isEqualTo(TEST_ROLE);
        assertThat(role2.getDescription()).isNull();

        var role3 = new Role(TEST_ROLE, null);
        assertThat(role3.getName()).isEqualTo(TEST_ROLE);
        assertThat(role3.getDescription()).isNull();

        var role4 = new Role(TEST_ROLE, "");
        assertThat(role4.getName()).isEqualTo(TEST_ROLE);
        assertThat(role4.getDescription()).isBlank();
    }

    @Test void testCreationNegative1() {
        assertThatThrownBy(() -> new Role("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("", "TEST")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role(null, "TEST")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testAddUser() {
        var role = new Role(TEST_ROLE);
        var user = new User(TEST_ROLE);
        role.addUser(user);
        assertThat(role.getUsers()).hasSize(1);
        assertThat(role.getUsers()).contains(user);
    }

    @Test void testAddUserNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.addUser(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testAddUsers() {
        var testee = new Role();
        testee.addUsers(Set.of(new User()));
        assertThat(testee.getUsers()).hasSize(1);
    }

    @Test void testAddUsersNegative() {
        var testee = new Role();
        assertThatThrownBy(() -> testee.addUsers(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testRemoveUser() {
        var role = new Role(TEST_ROLE);
        var user = new User(TEST_ROLE);
        role.addUser(user);
        assertThat(role.getUsers()).hasSize(1);
        role.removeUser(user);
        assertThat(role.getUsers()).isEmpty();
    }

    @Test void testRemoveUserNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.removeUser(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testSetUsers() {
        var role = new Role(TEST_ROLE);
        var user = new User(TEST_ROLE);
        assertThat(role.getUsers()).isEmpty();
        role.setUsers(new HashSet<>(Collections.singletonList(user)));
        assertThat(role.getUsers()).hasSize(1);
    }

    @Test void testSetUsersNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.setUsers(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testAddGrant() {
        var role = new Role(TEST_ROLE);
        var grant = new Grant(TEST_DESCR);
        assertThat(role.getGrants()).isEmpty();
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
    }

    @Test void testAddGrantNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.addGrant(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testRemoveGrant() {
        var role = new Role(TEST_ROLE);
        var grant = new Grant(TEST_ROLE);
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
        role.removeGrant(grant);
        assertThat(role.getGrants()).isEmpty();
    }

    @Test void testRemoveGrantNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.removeGrant(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testRemoveGrants() {
        var role = new Role(TEST_ROLE);
        var grant = new Grant(TEST_ROLE);
        role.addGrant(grant);
        assertThat(role.getGrants()).hasSize(1);
        role.removeGrants(Collections.singletonList(grant));
        assertThat(role.getGrants()).isEmpty();
    }

    @Test void testRemoveGrantsNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.removeGrants(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testSetGrants() {
        var role = new Role(TEST_ROLE);
        var grant = new Grant(TEST_ROLE);
        assertThat(role.getGrants()).isEmpty();
        role.setGrants(new HashSet<>(Collections.singletonList(grant)));
        assertThat(role.getGrants()).hasSize(1);
    }

    @Test void testSetGrantsNegative() {
        var role = new Role(TEST_ROLE);
        assertThatThrownBy(() -> role.setGrants(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
