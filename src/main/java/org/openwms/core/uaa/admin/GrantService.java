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

import org.openwms.core.uaa.admin.impl.Grant;
import org.openwms.core.uaa.api.ValidationGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A GrantService defines functionality to handle {@code GrantService}s.
 *
 * @author Heiko Scherrer
 */
public interface GrantService {

    /**
     * Merge a list of persisted, detached or transient {@link Grant}s of a particular {@code Module}.
     *
     * @param moduleName The moduleName
     * @param grants The list of {@link Grant}s to merge
     * @return All existing {@link Grant}s
     */
    @NotNull List<Grant> mergeGrants(@NotBlank String moduleName, @NotNull List<Grant> grants);

    /**
     * Find and return a {@code Grant}.
     *
     * @param pKey The persistent key of the existing Grant
     * @return The instance
     * @throws org.ameba.exception.NotFoundException If the Grant does not exist
     */
    @NotNull Grant findByPKey(@NotBlank String pKey);

    /**
     * Find and return all existing {@link Grant}s.
     *
     * @return All existing {@link Grant}s
     */
    @NotNull List<Grant> findAllGrants();

    /**
     * Find and return all {@link Grant}s assigned to an {@code User}.
     *
     * @param user The User's name
     * @return All Grants assigned to the User
     */
    @NotNull List<Grant> findAllFor(@NotBlank String user);

    /**
     * Create a new {@link Grant}.
     *
     * @param grant The mandatory Grant
     * @return The created Grant instance
     */
    @NotNull Grant create(@NotNull(groups = ValidationGroups.Create.class) @Valid Grant grant);
}
