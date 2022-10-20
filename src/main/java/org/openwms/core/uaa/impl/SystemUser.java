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
package org.openwms.core.uaa.impl;

import java.io.Serializable;

/**
 * A SystemUser is granted with all privileges and omits all defined security constraints. Whenever a SystemUser logs in, she is assigned to
 * a virtual {@link Role} with the name ROLE_SYSTEM. Furthermore this kind of {@link Role} is immutable and it is not allowed for the
 * SystemUser to change her {@link UserDetails} or {@link UserPassword}. Changing the {@link UserPassword} has to be done in the application
 * configuration when the project is setup.
 *
 * @author <a href="mailto:russelltina@users.sourceforge.net">Tina Russell</a>
 * @GlossaryTerm
 * @see User
 */
public class SystemUser extends User implements Serializable {

    /**
     * The defined fullname of the system user. Default {@value} .
     */
    public static final String SYSTEM_USERNAME = "openwms";
    /**
     * The virtual {@code Role} of the SystemUser.
     */
    public static final String SYSTEM_ROLE_NAME = "ROLE_SYSTEM";

    /**
     * Dear JPA...
     */
    protected SystemUser() {
    }

    /**
     * Create a new SystemUser.
     *
     * @param username SystemUser's username
     * @param password SystemUser's password
     */
    public SystemUser(String username, String password) {
        super(username, password);
        setFullname(SYSTEM_USERNAME);
    }

    /**
     * Check whether {@code user} is the system user.
     *
     * @param user The user to check
     * @return {@literal true} if user is the system user, otherwise {@literal false}
     */
    public static boolean isSuperUser(User user) {
        return (user instanceof SystemUser);
    }
}