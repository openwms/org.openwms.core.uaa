/*
 * Copyright 2005-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.uaa.admin;

import org.ameba.integration.FindOperations;
import org.ameba.integration.SaveOperations;
import org.openwms.core.exception.InvalidPasswordException;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.configuration.UserPreference;
import org.openwms.core.uaa.admin.impl.SystemUser;
import org.openwms.core.uaa.admin.impl.User;
import org.openwms.core.uaa.admin.impl.UserPassword;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * An UserService offers functionality according to the handling with {@link User}s.
 *
 * @author Heiko Scherrer
 */
public interface UserService extends FindOperations<User, Long>, SaveOperations<User, Long> {

    /**
     * Change the current {@link User}s password.
     *
     * @param userPassword The UserPassword to change
     */
    void changeUserPassword(@NotNull UserPassword userPassword);

    /**
     * Attach and save an {@code image} to an {@link User} with {@code id}.
     *
     * @param pKey The persistent key of the User
     * @param image Image to be stored
     */
    void uploadImageFile(String pKey, byte[] image);

    /**
     * Return a transient {@link User} entity object, serving as a template.
     *
     * @param username Username of the User
     * @return An empty User template
     */
    User getTemplate(String username);

    /**
     * Save changes on an {@link User} and additionally save the User's password and preferences.
     *
     * @param user The User to change
     * @param userPassword The Users password
     * @param prefs An array of UserPreference objects
     * @return The saved User instance
     */
    User saveUserProfile(@NotNull User user, @NotNull UserPassword userPassword, UserPreference... prefs);

    /**
     * Create and return the {@link SystemUser} without persisting this user.
     *
     * @return the SystemUser instance
     */
    SystemUser createSystemUser();

    /**
     * Create a non-existing User.
     *
     * @param user The User instance to create
     * @return The created instance
     */
    User create(@NotNull @Valid User user);

    /**
     * Find and return an {@code User} instance.
     *
     * @param username The unique name of the User to search for
     * @return The instance
     */
    Optional<User> findByUsername(@NotEmpty String username);

    /**
     * Find and return an {@code User} instance.
     *
     * @param pKey The persistent identifier of the User to search for
     * @return The instance
     */
    @NotNull User findByPKey(@NotEmpty String pKey);

    /**
     * Delete an {@link User}.
     *
     * @param username The User's unique name
     */
    void remove(String username);

    /**
     * Delete an {@link User}.
     *
     * @param pKey The identifiable persistent key
     */
    void delete(String pKey);

    /**
     *
     * @param pKey
     * @param newPassword
     * @return
     */
    UserVO updatePassword(String pKey, CharSequence newPassword) throws InvalidPasswordException;
}