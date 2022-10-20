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
package org.openwms.core.uaa;

import org.ameba.integration.FindOperations;
import org.ameba.integration.SaveOperations;
import org.openwms.core.uaa.impl.SystemUser;
import org.openwms.core.uaa.impl.User;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.api.ValidationGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * An UserService offers functionality according to the handling with {@link User}s.
 *
 * @author Heiko Scherrer
 */
public interface UserService extends FindOperations<User, Long>, SaveOperations<User, Long> {

    /**
     * Save or update an already existing {@link User}.
     *
     * @param user The instance to save
     * @param roleNames A list of Role names to assign the User to
     * @return The saved instance
     */
    @NotNull User save(@NotNull(groups = ValidationGroups.Modify.class) @Valid User user, List<String> roleNames);

    /**
     * Attach and save an {@code image} to an {@link User} with {@code id}.
     *
     * @param pKey The persistent key of the User
     * @param image Image to be stored
     */
    void uploadImageFile(@NotBlank String pKey, @NotNull byte[] image);

    /**
     * Create and return the {@link SystemUser} without persisting this user.
     *
     * @return the SystemUser instance
     */
    @NotNull SystemUser createSystemUser();

    /**
     * Create a non-existing User.
     *
     * @param user The User instance to create
     * @param roleNames A list of Role names to assign the User to
     * @return The created instance
     */
    @NotNull User create(@NotNull(groups = ValidationGroups.Create.class) @Valid User user, List<String> roleNames);

    /**
     * Find and return an {@code User} instance.
     *
     * @param username The unique name of the User to search for
     * @return The instance
     */
    @NotNull Optional<User> findByUsername(@NotBlank String username);

    /**
     * Find and return an {@code User} instance.
     *
     * @param pKey The persistent identifier of the User to search for
     * @return The instance
     */
    @NotNull User findByPKey(@NotBlank String pKey);

    /**
     * Delete an {@link User}.
     *
     * @param pKey The identifiable persistent key
     */
    void delete(@NotBlank String pKey);

    /**
     * Update the password of the {@link User}.
     *
     * @param pKey The identifiable persistent key
     * @param newPassword The new password
     * @return The updated instance
     */
    @NotNull UserVO updatePassword(@NotBlank String pKey, @NotNull CharSequence newPassword) throws InvalidPasswordException;
}