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
package org.openwms.core.uaa.admin.impl;

import org.springframework.security.core.GrantedAuthority;

/**
 * A SystemUserAuthority.
 *
 * @author Heiko Scherrer
 */
final class SystemUserAuthority implements GrantedAuthority {

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + SystemUser.SYSTEM_ROLE_NAME.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && (this == obj || getClass() == obj.getClass());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Return the System User's rolename.
     */
    @Override
    public String getAuthority() {
        return SystemUser.SYSTEM_ROLE_NAME;
    }
}