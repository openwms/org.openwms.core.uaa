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

import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.ValidationGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * A RoleService provides business functionality regarding the handling with {@link Role}s. The service deals directly with business
 * objects, even those are currently acting as entities as well. But this may change in future and should not influence the interface.
 *
 * @author Heiko Scherrer
 * @version 0.2
 * @see Role
 * @since 0.1
 */
public interface RoleService {

    /**
     * Find and return all existing Roles.
     *
     * @return All Roles or an empty collection type, never {@literal null}
     */
    List<RoleVO> findAll();

    /**
     * Create a Role that does not exist so far.
     *
     * @param role The Role to be created
     * @return The created Role instance
     * @throws org.ameba.exception.ResourceExistsException If the {@code role} already exists
     */
    RoleVO create(@NotNull(groups = ValidationGroups.Create.class) @Valid RoleVO role);

    /**
     * Update an existing Role.
     *
     * @param pKey The persistent key of the existing Role
     * @param role The Role to update
     * @return The updated instance
     * @throws org.ameba.exception.NotFoundException If the {@code role} does not exist
     */
    RoleVO save(@NotEmpty String pKey, @NotNull(groups = ValidationGroups.Modify.class) @Valid RoleVO role);

    /**
     * Delete a {@link Role}.
     *
     * @param pKey The identifiable persistent key
     */
    void delete(@NotEmpty String pKey);
}