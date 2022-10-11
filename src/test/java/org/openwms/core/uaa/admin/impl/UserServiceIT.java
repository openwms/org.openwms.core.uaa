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
import org.ameba.exception.NotFoundException;
import org.ameba.exception.ServiceLayerException;
import org.ameba.i18n.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.TestBase;
import org.openwms.core.uaa.MessageCodes;
import org.openwms.core.uaa.admin.EmailMapper;
import org.openwms.core.uaa.admin.UserMapper;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.configuration.ConfigurationService;
import org.openwms.core.uaa.configuration.UserPreference;
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
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A UserServiceIT.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(
        showSql = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConfigurationService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PasswordEncoder.class)
        }
)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback
@Import(ValidationConfiguration.class)
public class UserServiceIT extends TestBase {

    private static final String TEST_USER = "TEST";
    private static final String UNKNOWN_USER = "UNKNOWN";
    private static final String KNOWN_USER = "KNOWN";
    @Autowired
    private UserService srv;
    @Autowired
    private TestEntityManager entityManager;
    @MockBean
    private Translator translator;

    @TestConfiguration
    @EnablePluginRegistries({UserUpdater.class})
    public static class TestConfig {
        @Bean
        public UserMapper beanMapper(EmailMapper emailMapper) {
            return new UserMapperImpl(emailMapper);
        }
        @Bean
        public EmailMapper emailMapper() {
            return new EmailMapperImpl();
        }
        @Bean
        MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
            var mvpp = new MethodValidationPostProcessor();
            mvpp.setValidator(validator);
            return mvpp;
        }
        @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }
    }

    /**
     * Setting up some test users.
     */
    @BeforeEach
    public void onBefore() {
        entityManager.getEntityManager().persist(new User(KNOWN_USER));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testUploadImageNotFound() {
        assertThatThrownBy(() -> srv.uploadImageFile("100L", new byte[222])).isInstanceOf(NotFoundException.class);
    }

    @Test void testUploadImage() {
        srv.uploadImageFile(findUser(KNOWN_USER).getPersistentKey(), new byte[222]);
        var user = findUser(KNOWN_USER);
        assertThat(user.getUserDetails().getImage()).hasSize(222);
    }

    @Test void testSaveWithNull() {
        assertThatThrownBy(() -> srv.save(null)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void testSaveTransient() {
        var user = srv.save(new User(UNKNOWN_USER));
        assertThat(user.isNew()).isFalse();
    }

    @Test void testSaveDetached() {
        var user = findUser(KNOWN_USER);
        assertThat(user.isNew()).isFalse();
        entityManager.clear();
        user.setFullname("Mr. Hudson");
        srv.save(user);
        entityManager.flush();
        entityManager.clear();
        user = findUser(KNOWN_USER);
        assertThat(user.getFullname()).isEqualTo("Mr. Hudson");
    }

    @Test void testRemoveWithNull() {
        assertThatThrownBy(() -> srv.remove(null)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void testRemove() {
        var user = findUser(KNOWN_USER);
        assertThat(user.isNew()).isFalse();
        entityManager.clear();
        srv.remove(KNOWN_USER);
        entityManager.flush();
        entityManager.clear();
        assertThatThrownBy(() -> findUser(KNOWN_USER)).isInstanceOf(NoResultException.class);
    }

    @Test void testChangePasswordWithNull() {
        assertThatThrownBy(() -> srv.changeUserPassword(null)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void testChangePasswordUnknown() {
        assertThatThrownBy(() -> srv.changeUserPassword(new UserPassword(new User(UNKNOWN_USER), "password")))
                .isInstanceOf(NotFoundException.class)
                .extracting("messageKey")
                .isEqualTo(MessageCodes.USER_WITH_NAME_NOT_EXIST);
        ;
    }

    @Test void testChangePassword() {
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
    }

    @Test void testChangePasswordInvalidPassword() {
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password1"));
        assertThatThrownBy(() -> srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password")))
                .isInstanceOf(ServiceLayerException.class)
                .extracting("messageKey")
                .isEqualTo(MessageCodes.USER_PW_INVALID);
    }

    @Test void testFindAll() {
        assertThat(srv.findAll()).hasSizeGreaterThan(0);
    }

    @Test void testFindById() {
        var users = srv.findAll();
        assertThat(users).hasSizeGreaterThan(0);
        var user = srv.findById(users.iterator().next().getPk());
        assertThat(user).isNotNull();
    }

    @Test void testFindByIdNegative() {
        var users = srv.findAll();
        assertThat(users).hasSizeGreaterThan(0);
        assertThatThrownBy(() -> srv.findById(users.iterator().next().getPk() + 1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test void testGetTemplate() {
        var user = srv.getTemplate("TEST_USER");
        assertThat(user.isNew()).isTrue();
        assertThat(user.getUsername()).isEqualTo("TEST_USER");
    }

    @Test void testCreateSystemUser() {
        var user = srv.createSystemUser();
        assertThat(user.isNew()).isTrue();
        assertThat(user.getRoles()).hasSize(1);
    }

    @Test void testSaveUserProfileUserNull() {
        assertThatThrownBy(
                () -> srv.saveUserProfile(null, new UserPassword(new User(TEST_USER), TEST_USER))
        ).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void testSaveUserProfileUserPreferencePasswordNull() {
        assertThatThrownBy(
                () -> srv.saveUserProfile(new User(TEST_USER), null)
        ).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void testSaveUserProfileUserWithPassword() {
        var user = new User(TEST_USER);
        var u = srv.saveUserProfile(user, new UserPassword(user, "password"));
        assertThat(user).isEqualTo(u);
    }

    @Test void testSaveUserProfileUserWithInvalidPassword() {
        var user = new User(TEST_USER);
        var u = srv.saveUserProfile(user, new UserPassword(user, "password"));
        u = srv.saveUserProfile(u, new UserPassword(u, "password1"));
        final var savedUser = srv.findByUsername(TEST_USER).orElseThrow();
        assertThatThrownBy(() -> srv.saveUserProfile(savedUser, new UserPassword(user, "password")))
                .isInstanceOf(ServiceLayerException.class)
                .extracting("messageKey")
                .isEqualTo(MessageCodes.USER_PW_INVALID);
    }

    @Test void testSaveUserProfileWithPreference() {
        var user = new User(TEST_USER);
        var u = srv.saveUserProfile(user, new UserPassword(user, "password"), new UserPreference(user.getUsername()));
        entityManager.flush();
        entityManager.clear();
        assertThat(user).isEqualTo(u);
        /*
        assertEquals("Number of UserPreferences must be 1", 1, entityManager.find(User.class, u.getId())
                .getPreferences().size());
                */
    }

    private User findUser(String userName) {
        return (User) entityManager.getEntityManager().createQuery("select u from User u where u.username = :name").setParameter("name", userName)
                .getSingleResult();
    }
}