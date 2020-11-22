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
package org.openwms.core.uaa.api;

/**
 * A UAAConstants.
 *
 * @author Heiko Scherrer
 */
public final class UAAConstants {

    /** API root to hit Clients (plural). */
    public static final String API_CLIENTS = "/api/clients";
    /** API root to hit Grants (plural). */
    public static final String API_GRANTS = "/grants";
    /** API root to hit Roles (plural). */
    public static final String API_ROLES = "/roles";
    /** API root to hit Users (plural). */
    public static final String API_USERS = "/users";

    private UAAConstants() {
    }
}
