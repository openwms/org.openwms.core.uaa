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
import java.util.Set;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#equals(java.lang.Object)} .
     */
    @Test final void testEqualsObject() {
        Grant grant1 = new Grant(GRANT_NAME1, "abc");
        Grant grant2 = new Grant(GRANT_NAME2, "defg");
        Grant grant3 = new Grant(GRANT_NAME3, "hijkl");

        // Just the name is considered
        assertEquals(grant1, grant2);
        assertNotEquals(grant1, new Object());
        assertEquals(grant1, grant1);
        assertNotEquals(grant1, grant3);

        // Test behavior in hashed collections
        Set<Grant> grants = new HashSet<>();
        grants.add(grant1);
        grants.add(grant2);
        assertEquals(1, grants.size());
        grants.add(grant3);
        assertEquals(2, grants.size());
    }

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#Grant()}.
     */
    @Test final void testGrant() {
        Grant grant = new Grant();
        assertNull(grant.getName());
        assertNull(grant.getDescription());
    }

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#Grant(java.lang.String, java.lang.String)} .
     */
    @Test final void testGrantStringString() {
        Grant grant = new Grant(GRANT_NAME1, GRANT_DESC1);
        assertEquals(GRANT_NAME1, grant.getName());
        assertEquals(GRANT_DESC1, grant.getDescription());
    }

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#Grant(java.lang.String, java.lang.String)} .
     */
    @Test final void testGrantStringStringEmpty() {
        try {
            new Grant(null, GRANT_DESC1);
            fail("IAE expected when creating Grant(String, String) with name equals to null");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new Grant("", GRANT_DESC1);
            fail("IAE expected when creating Grant(String, String) with empty name");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#Grant(java.lang.String)} .
     */
    @Test final void testGrantString() {
        Grant grant = new Grant(GRANT_NAME1);
        assertEquals(GRANT_NAME1, grant.getName());
        assertNull(grant.getDescription());
    }

    /**
     * Test method for {@link org.openwms.core.system.usermanagement.Grant#Grant(java.lang.String)} .
     */
    @Test final void testGrantStringEmpty() {
        try {
            new Grant(null);
            fail("IAE expected when creating Grant(String) with name equals to null");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new Grant("");
            fail("IAE expected when creating Grant(String) with empty name");
        } catch (IllegalArgumentException iae) {
        }
    }
}