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
package org.openwms.core.uaa;

import org.openwms.core.uaa.impl.Role;

import javax.validation.constraints.NotNull;
import java.util.Collection;

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
    Collection<Role> findAll();

    /**
     * Save a Role passed as argument {@literal role}. Is the {@literal role} instance already exists it's being updated. If it does not
     * exist it will be created.
     *
     * @param role The Role to update or create
     * @return The updated Role instance
     */
    Role save(@NotNull Role role);
}