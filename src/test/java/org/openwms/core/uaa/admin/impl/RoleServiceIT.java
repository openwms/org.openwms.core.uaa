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

import org.ameba.app.ValidationConfiguration;
import org.ameba.exception.ResourceExistsException;
import org.ameba.i18n.Translator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.TestBase;
import org.openwms.core.uaa.admin.RoleMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.api.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import static java.util.Arrays.asList;
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
@Import(ValidationConfiguration.class)
public class RoleServiceIT extends TestBase {

    @Autowired
    private TestEntityManager entityManager;
    @MockBean
    private UserService userService;
    @MockBean
    private Translator translator;
    @Autowired
    private RoleService testee;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public RoleMapper beanMapper() {
            return new RoleMapperImpl(new UserMapperImpl(new EmailMapperImpl(), new UserDetailsMapperImpl()), new SecurityObjectMapperImpl());
        }
        @Bean
        MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
            var mvpp = new MethodValidationPostProcessor();
            mvpp.setValidator(validator);
            return mvpp;
        }
    }

    @Test void testCreateWithNull() {
        assertThrows(ConstraintViolationException.class, () -> testee.create(null));
    }

    @Test void testCreateWithoutNameMustFail() {
        assertThrows(ConstraintViolationException.class, () -> testee.create(new RoleVO()));
    }

    @Test void testCreateExistingRoleMustFail() {
        assertThrows(ResourceExistsException.class, () -> testee.create(RoleVO.newBuilder().name("ROLE_ADMIN").build()));
    }

    @Test void testCreateNewRole() {
        var role = testee.create(RoleVO.newBuilder().name("ANONYMOUS").build());
        assertThat(role).isNotNull();
        assertThat(role.getName()).isEqualTo("ROLE_ANONYMOUS");
    }

    @Test void testCreateNewRoleWithoutPrefix() {
        var role = testee.create(RoleVO.newBuilder().name("ANONYMOUS").build());
        assertThat(role.getName()).isEqualTo("ROLE_ANONYMOUS");
    }

    @Test void testSaveWithNulls() {
        assertThrows(ConstraintViolationException.class, () -> testee.save(null, null));
        assertThrows(ConstraintViolationException.class, () -> testee.save("", null));
    }

    @Test void testSaveNotExistingRole() {
        assertThrows(ConstraintViolationException.class, () -> testee.save("UNKNOWN", null));
    }

    @Test void testSaveExisingRole() {
        var roleSaved = testee.save("1", RoleVO.newBuilder().name("test").description("Test description").build());
        assertThat(roleSaved).isNotNull();
        assertThat(roleSaved.getDescription()).isEqualTo("Test description");
        assertThat(roleSaved.getName()).isEqualTo("ROLE_test");
    }

    @Test void testFindAll() {
        assertThat(testee.findAll()).hasSize(2);
    }

    @Test void testFindByNames() {
        assertThat(testee.findByNames(asList("ROLE_ADMIN", "ROLE_OPS", "SEC_UAA_USER_LOOKUP"))).hasSize(2);
    }
}