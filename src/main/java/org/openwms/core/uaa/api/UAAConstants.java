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
