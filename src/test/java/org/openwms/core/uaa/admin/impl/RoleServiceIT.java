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

import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.TestBase;
import org.openwms.core.uaa.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

/**
 * A RoleServiceIT.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(
        showSql = false,
        includeFilters =
                {
                        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RoleService.class)
                }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleServiceIT extends TestBase {

    @Autowired
    private RoleService srv;
    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public BeanMapper beanMapper() {
            return new DozerMapperImpl("META-INF/dozer/bean-mappings.xml");
        }
    }

    /**
     * Setting up some test data.
     */
    @BeforeEach
    public void onBefore() {
        entityManager.persist(new Role("ROLE_ADMIN"));
        entityManager.persist(new Role("ROLE_USER"));
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Test to call save with null argument.
     */
    @Disabled
    public final void testSaveWithNull() {
        assertThrows(ConstraintViolationException.class, () -> srv.save(null));
    }

    /**
     * Test to save a transient role.
     */
    public final void testSaveTransient() {
        Role role = null;
        try {
            role = srv.save(new Role("ROLE_ANONYMOUS"));
        } catch (Exception e) {
            fail("Exception thrown during saving a role");
        }
        assertNotNull("Expected to return a role", role);
        assertFalse("Expect the role as persisted", role.isNew());
    }

    /**
     * Test to save a detached role.
     */
    @Test
    public final void testSaveDetached() {
        Role role = findRole("ROLE_ADMIN");
        Role roleSaved = null;
        role.setDescription("Test description");
        try {
            roleSaved = srv.save(role);
            entityManager.flush();
        } catch (Exception e) {
            fail("Exception thrown during saving a role");
        }
        assertNotNull("Expected to return a role", roleSaved);
        assertFalse("Expect the role as persisted", roleSaved.isNew());
        Assertions.assertEquals("Expected that description was saved", "Test description", roleSaved.getDescription());
    }

    /**
     * Test findAll.
     */
    @Test
    public final void testFindAll() {
        assertEquals("2 Roles are expected", 2, srv.findAll().size());
    }

    private Role findRole(String roleName) {
        return (Role) entityManager.getEntityManager().createQuery("select r from Role r where r.name = :name").setParameter("name", roleName)
                .getSingleResult();
    }
}