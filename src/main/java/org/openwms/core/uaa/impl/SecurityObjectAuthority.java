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

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * A SecurityObjectAuthority.
 * 
 * @author Heiko Scherrer
 * @see org.springframework.security.core.GrantedAuthority
 */
class SecurityObjectAuthority implements GrantedAuthority, Serializable {

    private final String sObj;

    /**
     * Create a new SecurityObjectAuthority.
     * 
     * @param securityObject
     *            A {@link SecurityObject} to use as authority carrier.
     */
    public SecurityObjectAuthority(SecurityObject securityObject) {
        sObj = securityObject.getName();
    }

    public SecurityObjectAuthority(String sObj) {
        this.sObj = sObj;
    }

    /**
     * {@inheritDoc}
     * 
     * Return the name of the wrapped {@link SecurityObject}.
     */
    @Override
    public String getAuthority() {
        return sObj;
    }

    /**
     * {@inheritDoc}
     * 
     * Uses sObj for calculation.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((sObj == null) ? 0 : sObj.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * Uses sObj for comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (SecurityObjectAuthority) obj;
        if (sObj == null) {
            if (other.sObj != null) {
                return false;
            }
        } else if (!sObj.equals(other.sObj)) {
            return false;
        }
        return true;
    }

    /**
     * Delegate to the wrapped SecurityObject.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return sObj;
    }

}