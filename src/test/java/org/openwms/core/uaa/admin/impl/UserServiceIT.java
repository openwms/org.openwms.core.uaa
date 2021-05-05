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

import org.ameba.exception.NotFoundException;
import org.ameba.exception.ServiceLayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openwms.core.TestBase;
import org.openwms.core.uaa.app.UAAModuleConfiguration;
import org.openwms.core.uaa.configuration.ConfigurationService;
import org.openwms.core.uaa.configuration.UserPreference;
import org.openwms.core.exception.ExceptionCodes;
import org.openwms.core.uaa.admin.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

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
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConfigurationService.class)
        }
)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback
@Import(UAAModuleConfiguration.class)
public class UserServiceIT extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceIT.class);
    private static final String TEST_USER = "TEST";
    private static final String UNKNOWN_USER = "UNKNOWN";
    private static final String KNOWN_USER = "KNOWN";
    @Autowired
    private UserService srv;
    @Autowired
    private TestEntityManager entityManager;

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
        assertThrows(NotFoundException.class, () -> srv.uploadImageFile("100L", new byte[222]));
    }

    @Test void testUploadImage() {
        srv.uploadImageFile(findUser(KNOWN_USER).getPersistentKey(), new byte[222]);
        User user = findUser(KNOWN_USER);
        assertThat(user.getUserDetails().getImage()).hasSize(222);
    }

    @Disabled
    @Test void testSaveWithNull() {
        try {
            srv.save(null);
            fail("Should throw an exception when calling with null");
        } catch (ServiceLayerException sre) {
            LOGGER.debug("OK: null user:" + sre.getMessage());
        }
    }

    @Test void testSaveTransient() {
        User user = srv.save(new User(UNKNOWN_USER));
        assertFalse("User must be persisted and has a primary key", user.isNew());
    }

    @Test void testSaveDetached() {
        User user = findUser(KNOWN_USER);
        assertFalse("User must be persisted before", user.isNew());
        entityManager.clear();
        user.setFullname("Mr. Hudson");
        user = srv.save(user);
        entityManager.flush();
        entityManager.clear();
        user = findUser(KNOWN_USER);
        assertEquals("Mr. Hudson", user.getFullname(), "Changes must be saved");
    }

    @Disabled
    @Test void testRemoveWithNull() {
        try {
            srv.remove(null);
            fail("Should throw an exception when calling with null");
        } catch (ServiceLayerException sre) {
            LOGGER.debug("OK: null user:" + sre.getMessage());
        }
    }

    @Test void testRemove() {
        User user = findUser(KNOWN_USER);
        assertThat(user.isNew());
        entityManager.clear();
        srv.remove(KNOWN_USER);
        entityManager.flush();
        entityManager.clear();
        assertThrows(NoResultException.class, () -> findUser(KNOWN_USER));
    }

    @Disabled
    public final void testChangePasswordWithNull() {
        assertThrows(ConstraintViolationException.class, () -> srv.changeUserPassword(null));
    }

    @Test void testChangePasswordUnknown() {
        try {
            srv.changeUserPassword(new UserPassword(new User(UNKNOWN_USER), "password"));
            fail("Should throw an exception when calling with an unknown user");
        } catch (NotFoundException sre) {
            if (!(sre.getMessageKey().equals(ExceptionCodes.USER_NOT_EXIST))) {
                fail("Should throw an NotFoundException when calling with an unknown user");
            }
            LOGGER.debug("OK: UserNotFoundException:" + sre.getMessage());
        }
    }

    @Test void testChangePassword() {
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
    }

    @Test void testChangePasswordInvalidPassword() {
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password1"));
        try {
            srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
            fail("Should throw an exception when calling with an invalid password");
        } catch (ServiceLayerException sre) {
            if (!(sre.getMessageKey().equals(ExceptionCodes.USER_PW_INVALID))) {
                fail("Should throw a nested InvalidPasswordException when calling with an invalid password");
            }
            LOGGER.debug("OK: InvalidPasswordException:" + sre.getMessage());
        }
    }

    public
    @Test void testFindAll() {
        assertThat(srv.findAll()).hasSize(1);
    }

    @Test void testFindById() {
        Collection<User> users = srv.findAll();
        assertEquals(1, users.size(), "1 User is expected");
        User user = srv.findById(users.iterator().next().getPk());
        assertNotNull("We expect to get back an instance", user);
    }

    @Test void testFindByIdNegative() {
        Collection<User> users = srv.findAll();
        assertEquals(1, users.size(), "1 User is expected");
        assertThrows(RuntimeException.class, () -> srv.findById(users.iterator().next().getPk() + 1));
    }

    @Test void testGetTemplate() {
        User user = srv.getTemplate("TEST_USER");
        assertTrue("Must be a new User", user.isNew());
        assertEquals("TEST_USER", user.getUsername(), "Expected to get an User instance with the same username");
    }

    @Test void testCreateSystemUser() {
        User user = srv.createSystemUser();
        assertTrue("Must be a new User", user.isNew());
        assertEquals(1, user.getRoles().size(), "Expected one Role");
    }

    @Disabled
    @Test void testSaveUserProfileUserNull() {
        assertThrows(ConstraintViolationException.class, () -> srv.saveUserProfile(null, new UserPassword(new User(TEST_USER), TEST_USER)));
    }

    @Disabled
    @Test void testSaveUserProfileUserPreferencePasswordNull() {
        assertThrows(ConstraintViolationException.class, () -> srv.saveUserProfile(new User(TEST_USER), null));
    }

    @Test void testSaveUserProfileUserWithPassword() {
        User user = new User(TEST_USER);
        User u = srv.saveUserProfile(user, new UserPassword(user, "password"));
        assertEquals(u, user, "Must return the saved user");
    }

    @Test void testSaveUserProfileUserWithInvalidPassword() {
        User user = new User(TEST_USER);
        User u = srv.saveUserProfile(user, new UserPassword(user, "password"));
        u = srv.saveUserProfile(u, new UserPassword(u, "password1"));
        try {
            u = srv.saveUserProfile(u, new UserPassword(user, "password"));
            fail("Expected to catch an ServiceLayerException when the password is invalid");
        } catch (ServiceLayerException sre) {
            if (!(sre.getMessageKey().equals(ExceptionCodes.USER_PW_INVALID))) {
                fail("Expected to catch an InvalidPasswordException when the password is invalid");
            }
        }
        assertEquals(u, user, "Must return the saved user");
    }

    @Test void testSaveUserProfileWithPreference() {
        User user = new User(TEST_USER);
        User u = srv.saveUserProfile(user, new UserPassword(user, "password"), new UserPreference(user.getUsername()));
        entityManager.flush();
        entityManager.clear();
        assertEquals(u, user, "Must return the saved user");
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