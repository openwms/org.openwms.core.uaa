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

import org.ameba.app.BaseConfiguration;
import org.ameba.exception.ResourceExistsException;
import org.ameba.mapping.BeanMapper;
import org.ameba.mapping.DozerMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.TestBase;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.api.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
@Sql("classpath:test.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleServiceIT extends TestBase {

    @Autowired
    private RoleService srv;
    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    @Import(BaseConfiguration.class)
    public static class TestConfig {
        @Bean
        public BeanMapper beanMapper() {
            return new DozerMapperImpl("META-INF/dozer/bean-mappings.xml");
        }
        @Bean
        MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
            MethodValidationPostProcessor mvpp = new MethodValidationPostProcessor();
            mvpp.setValidator(validator);
            return mvpp;
        }
    }

    @Test void testCreateWithNull() {
        assertThrows(ConstraintViolationException.class, () -> srv.create(null));
    }

    @Test void testCreateWithoutNameMustFail() {
        assertThrows(ConstraintViolationException.class, () -> srv.create(new RoleVO()));
    }

    @Test void testCreateExistingRoleMustFail() {
        assertThrows(ResourceExistsException.class, () -> srv.create(RoleVO.newBuilder().name("ROLE_ADMIN").build()));
    }

    @Test void testCreateNewRole() {
        RoleVO role = srv.create(RoleVO.newBuilder().name("ANONYMOUS").build());
        assertThat(role).isNotNull();
        assertThat(role.getName()).isEqualTo("ROLE_ANONYMOUS");
    }

    @Test void testCreateNewRoleWithoutPrefix() {
        RoleVO role = srv.create(RoleVO.newBuilder().name("ANONYMOUS").build());
        assertThat(role.getName()).isEqualTo("ROLE_ANONYMOUS");
    }

    @Test void testSaveWithNulls() {
        assertThrows(ConstraintViolationException.class, () -> srv.save(null, null));
        assertThrows(ConstraintViolationException.class, () -> srv.save("", null));
    }

    @Test void testSaveNotExistingRole() {
        assertThrows(ConstraintViolationException.class, () -> srv.save("UNKNOWN", null));
    }

    @Test void testSaveExisingRole() {
        RoleVO roleSaved = srv.save("1", RoleVO.newBuilder().name("test").description("Test description").build());
        assertThat(roleSaved).isNotNull();
        assertThat(roleSaved.getDescription()).isEqualTo("Test description");
        assertThat(roleSaved.getName()).isEqualTo("ROLE_test");
    }

    @Test void testFindAll() {
        assertThat(srv.findAll()).hasSize(2);
    }
}