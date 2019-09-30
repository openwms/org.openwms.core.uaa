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
package org.openwms.core.uaa.impl;

import org.ameba.exception.NotFoundException;
import org.ameba.exception.ServiceLayerException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openwms.core.configuration.ConfigurationService;
import org.openwms.core.configuration.UserPreference;
import org.openwms.core.exception.ExceptionCodes;
import org.openwms.core.uaa.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A UserServiceIT.
 *
 * @author Heiko Scherrer
 */
@RunWith(SpringRunner.class)
@DataJpaTest(
        showSql = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConfigurationService.class)
        }
)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class UserServiceIT {

    @TestConfiguration
    @ComponentScan(basePackages = "org.openwms.core.uaa")
    public static class Config {
        @Bean
        LocalValidatorFactoryBean validatorFactoryBean() {
            return new LocalValidatorFactoryBean();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(15);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceIT.class);
    private static final String TEST_USER = "TEST";
    private static final String UNKNOWN_USER = "UNKNOWN";
    private static final String KNOWN_USER = "KNOWN";
    @Autowired
    private UserService srv;
    @Autowired
    private TestEntityManager entityManager;

    public @Rule ExpectedException thrown = ExpectedException.none();

    /**
     * Setting up some test users.
     */
    @Before
    public void onBefore() {
        entityManager.getEntityManager().persist(new User(KNOWN_USER));
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Test to save a byte array as image file.
     */
    public
    @Test
    final void testUploadImageNotFound() {
        thrown.expect(NotFoundException.class);
        srv.uploadImageFile(100L, new byte[222]);
    }

    /**
     * Test to save a byte array as image file.
     */
    public
    @Test
    final void testUploadImage() {
        srv.uploadImageFile(findUser(KNOWN_USER).getPk(), new byte[222]);
        User user = findUser(KNOWN_USER);
        assertThat(user.getUserDetails().getImage()).hasSize(222);
    }

    /**
     * Test to save a NULL user.
     */
    @Ignore // fix as next
    @Test
    public final void testSaveWithNull() {
        try {
            srv.save(null);
            fail("Should throw an exception when calling with null");
        } catch (ServiceLayerException sre) {
            LOGGER.debug("OK: null user:" + sre.getMessage());
        }
    }

    /**
     * Test to save a transient user.
     */
    @Test
    public final void testSaveTransient() {
        User user = srv.save(new User(UNKNOWN_USER));
        assertFalse("User must be persisted and has a primary key", user.isNew());
    }

    /**
     * Test to save a existing detached user.
     */
    @Test
    public final void testSaveDetached() {
        User user = findUser(KNOWN_USER);
        assertFalse("User must be persisted before", user.isNew());
        entityManager.clear();
        user.setFullname("Mr. Hudson");
        user = srv.save(user);
        entityManager.flush();
        entityManager.clear();
        user = findUser(KNOWN_USER);
        assertEquals("Changes must be saved", "Mr. Hudson", user.getFullname());
    }

    /**
     * Test to call remove with null.
     */
    @Ignore // fix as next
    @Test
    public final void testRemoveWithNull() {
        try {
            srv.remove(null);
            fail("Should throw an exception when calling with null");
        } catch (ServiceLayerException sre) {
            LOGGER.debug("OK: null user:" + sre.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public final void testRemove() {
        User user = findUser(KNOWN_USER);
        assertThat(user.isNew());
        entityManager.clear();
        srv.remove(KNOWN_USER);
        entityManager.flush();
        entityManager.clear();
        thrown.expect(NoResultException.class);
        findUser(KNOWN_USER);
    }

    /**
     * Test method for {@link UserServiceImpl#changeUserPassword(UserPassword)} .
     * <p>
     * Test to call with null.
     */
    @Ignore // fix as next
    @Test(expected = ConstraintViolationException.class)
    public final void testChangePasswordWithNull() {
        srv.changeUserPassword(null);
    }

    /**
     * Test method for {@link UserServiceImpl#changeUserPassword(UserPassword)} .
     * <p>
     * Test to change it for an unknown user.
     */
    @Test
    public final void testChangePasswordUnknown() {
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

    /**
     * Test method for {@link UserServiceImpl#changeUserPassword(UserPassword)} .
     * <p>
     * Test to change the password of an User.
     */
    @Test
    public final void testChangePassword() {
        srv.changeUserPassword(new UserPassword(new User(KNOWN_USER), "password"));
    }

    /**
     * Test method for {@link UserServiceImpl#changeUserPassword(UserPassword)} .
     * <p>
     * Test to change password to an invalid one.
     */
    @Test
    public final void testChangePasswordInvalidPassword() {
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

    /**
     * Test method for {@link UserServiceImpl#findAll()}.
     */
    public
    @Test
    final void testFindAll() {
        assertThat(srv.findAll()).hasSize(1);
    }

    /**
     * Test to verify that the previously User can be found by it's assigned technical key.
     */
    @Test
    public final void testFindById() {
        Collection<User> users = srv.findAll();
        assertEquals("1 User is expected", 1, users.size());
        User user = srv.findById(users.iterator().next().getPk());
        assertNotNull("We expect to get back an instance", user);
    }

    /**
     * This test tries to find an User with a non existing technical id. This must end up in any kind of RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public final void testFindByIdNegative() {
        Collection<User> users = srv.findAll();
        assertEquals("1 User is expected", 1, users.size());
        srv.findById(users.iterator().next().getPk() + 1);
        fail("We expect to run into some kind of RuntimeException when search for an User with a technical key greater than that previously assigned one, because that User should not exist");
    }

    /**
     * Test method for {@link UserServiceImpl#getTemplate(String)} .
     */
    @Test
    public final void testGetTemplate() {
        User user = srv.getTemplate("TEST_USER");
        assertTrue("Must be a new User", user.isNew());
        assertEquals("Expected to get an User instance with the same username", "TEST_USER", user.getUsername());
    }

    /**
     * Test method for {@link UserServiceImpl#createSystemUser()} .
     */
    @Test
    public final void testCreateSystemUser() {
        User user = srv.createSystemUser();
        assertTrue("Must be a new User", user.isNew());
        assertTrue("Must be a SystemUser", user instanceof SystemUser);
        assertEquals("Expected one Role", 1, user.getRoles().size());
    }

    /**
     *
     */
    @Ignore // fix as next
    @Test(expected = ConstraintViolationException.class)
    public final void testSaveUserProfileUserNull() {
            srv.saveUserProfile(null, new UserPassword(new User(TEST_USER), TEST_USER));
    }

    /**
     *
     */
    @Ignore // fix as next
    @Test(expected = ConstraintViolationException.class)
    public final void testSaveUserProfileUserPreferencePasswordNull() {
        srv.saveUserProfile(new User(TEST_USER), null);
    }

    /**
     *
     */
    @Test
    public final void testSaveUserProfileUserWithPassword() {
        User user = new User(TEST_USER);
        User u = srv.saveUserProfile(user, new UserPassword(user, "password"));
        assertEquals("Must return the saved user", u, user);
    }

    /**
     *
     */
    @Test
    public final void testSaveUserProfileUserWithInvalidPassword() {
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
        assertEquals("Must return the saved user", u, user);
    }

    /**
     *
     */
    @Test
    public final void testSaveUserProfileWithPreference() {
        User user = new User(TEST_USER);
        User u = srv.saveUserProfile(user, new UserPassword(user, "password"), new UserPreference(user.getUsername()));
        entityManager.flush();
        entityManager.clear();
        assertEquals("Must return the saved user", u, user);
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