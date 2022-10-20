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

import org.ameba.annotation.Default;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.beans.ConstructorProperties;
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
    @Default
    @ConstructorProperties({"name", "description"})
    public Grant(String name, String description) {
        super(name, description);
    }

    /**
     * Create a new Grant.
     * 
     * @param name
     *            The name of the {@code Grant}
     */
    @ConstructorProperties("name")
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
        var other = (Grant) obj;
        if (this.getName() == null) {
            return other.getName() == null;
        } else return this.getName().equals(other.getName());
    }
}