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

import java.io.Serializable;
import java.util.Collection;

/**
 * A SystemUserWrapper.
 *
 * @author Heiko Scherrer
 */
public class SystemUserWrapper extends UserWrapper implements Serializable {

    private String password;

    /**
     * Create a new SystemUserWrapper.
     *
     * @param user The wrapped user
     */
    public SystemUserWrapper(User user) {
        super(user);
    }

    /**
     * Get the password.
     *
     * @return this password or the password, set in the superclass
     */
    @Override
    public String getPassword() {
        return password == null ? super.getPassword() : password;
    }

    /**
     * Set the password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     * <p>
     * For the SystemUser account always add the {@link SystemUser#SYSTEM_ROLE_NAME} to the
     * collection of authorities.
     */
    @Override
    protected void addDefaultGrants(Collection<GrantedAuthority> authorities) {
        authorities.add(new SystemUserAuthority());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use password field in addition to inherited fields.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use password field for comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        var other = (SystemUserWrapper) obj;
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        return true;
    }
}