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
package org.openwms.core.uaa.admin;

import org.openwms.core.uaa.admin.impl.Grant;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A SecurityService defines functionality to handle {@code SecurityObject}s, especially {@code Grant}s.
 *
 * @author Heiko Scherrer
 * @version 0.2
 * @since 0.1
 */
public interface SecurityService {

    /**
     * Merge a list of persisted, detached or transient {@link Grant}s of a particular {@code Module}.
     *
     * @param moduleName The moduleName
     * @param grants The list of {@link Grant}s to merge
     * @return All existing {@link Grant}s
     */
    List<Grant> mergeGrants(@NotNull String moduleName, List<Grant> grants);

    List<Grant> findAllGrants();
}