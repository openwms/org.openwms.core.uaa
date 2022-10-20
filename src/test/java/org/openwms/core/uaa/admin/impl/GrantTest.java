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

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A GrantTest.
 *
 * @author Heiko Scherrer
 */
class GrantTest {

    private static final String GRANT_NAME1 = "GRANT_NAME1";
    private static final String GRANT_NAME2 = "GRANT_NAME1";
    private static final String GRANT_NAME3 = "GRANT_NAME3";
    private static final String GRANT_DESC1 = "GRANT_DESC1";

    @Test void testEqualsObject() {
        var grant1 = new Grant(GRANT_NAME1, "abc");
        var grant2 = new Grant(GRANT_NAME2, "defg");
        var grant3 = new Grant(GRANT_NAME3, "hijkl");

        // Just the name is considered
        assertThat(grant1).isEqualTo(grant2)
                .isNotEqualTo(new Object())
                .isEqualTo(grant1)
                .isNotEqualTo(grant3);

        // Test behavior in hashed collections
        var grants = new HashSet<>();
        grants.add(grant1);
        grants.add(grant2);
        assertThat(grants).hasSize(1);
        grants.add(grant3);
        assertThat(grants).hasSize(2);
    }

    @Test void testGrant() {
        var grant = new Grant();
        assertThat(grant.getName()).isNull();
        assertThat(grant.getDescription()).isNull();
    }

    @Test void testGrantStringString() {
        var grant = new Grant(GRANT_NAME1, GRANT_DESC1);
        assertThat(grant.getName()).isEqualTo(GRANT_NAME1);
        assertThat(grant.getDescription()).isEqualTo(GRANT_DESC1);
    }

    @Test void testGrantStringStringEmpty() {
        assertThatThrownBy(() -> new Grant(null, GRANT_DESC1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Grant("", GRANT_DESC1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testGrantString() {
        var grant = new Grant(GRANT_NAME1);
        assertThat(grant.getName()).isEqualTo(GRANT_NAME1);
        assertThat(grant.getDescription()).isNull();
    }

    @Test void testGrantStringEmpty() {
        assertThatThrownBy(() -> new Grant(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Grant("")).isInstanceOf(IllegalArgumentException.class);
    }
}