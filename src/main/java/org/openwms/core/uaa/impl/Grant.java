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
package org.openwms.core.uaa.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * A Grant gives permission to access some kind of application object. Grants to security aware application objects can be permitted or
 * denied for a certain {@code Role}, depending on the security configuration. Usually {@code Grant}s are assigned to a
 * {@code Role} and on or more {@code User} s are assigned to each {@code Role}s. A Grant is security aware, that means it is
 * an concrete {@code SecurityObject}.
 * <p>
 * Permissions to UI actions are managed with {@code Grant}s.
 * </p>
 * 
 * @GlossaryTerm
 * @author Heiko Scherrer
 * @version 0.2
 * @since 0.1
 * @see User
 * @see Role
 * @see SecurityObject
 */
@Entity
@DiscriminatorValue("GRANT")
public class Grant extends SecurityObject implements Serializable {

    /**
     * Dear JPA...
     */
    protected Grant() {
    }

    /**
     * Create a new Grant.
     * 
     * @param name
     *            The name of the {@code Grant}
     * @param description
     *            The description text of the {@code Grant}
     */
    public Grant(String name, String description) {
        super(name, description);
    }

    /**
     * Create a new Grant.
     * 
     * @param name
     *            The name of the {@code Grant}
     */
    public Grant(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * 
     * Use the hashCode of the superclass with the hashCode of 'GRANT' to distinguish between {@code Grant}s and other
     * {@code SecurityObject}s like {@code Role}s.
     * 
     * @see SecurityObject#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = super.hashCode();
        result = prime * result + "GRANT".hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see SecurityObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Grant)) {
            return false;
        }
        Grant other = (Grant) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }
}