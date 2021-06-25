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

import org.ameba.integration.jpa.ApplicationEntity;
import org.openwms.core.uaa.api.ValidationGroups;
import org.openwms.core.values.CoreTypeDefinitions;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * A SecurityObject is the generalization of {@code Role}s and {@code Grant}s and combines common used properties of both.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see Role
 * @see Grant
 */
@Entity
@Table(name = "COR_UAA_ROLE", uniqueConstraints = @UniqueConstraint(name = "UC_UAA_ROLE", columnNames = {"C_NAME"}))
@Inheritance
@DiscriminatorColumn(name = "C_TYPE", length = 20)
public class SecurityObject extends ApplicationEntity implements Serializable {

    /** Unique name of the {@code SecurityObject}. */
    @Column(name = "C_NAME", nullable = false)
    @OrderBy
    @NotEmpty(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private String name;

    /** A descriptive text for the {@code SecurityObject}. */
    @Column(name = "C_DESCRIPTION", length = CoreTypeDefinitions.DESCRIPTION_LENGTH)
    private String description;

    /* ----------------------------- methods ------------------- */

    /** Dear JPA... */
    protected SecurityObject() { }

    /**
     * Create a new {@code SecurityObject} with a name.
     *
     * @param name The name of the {@code SecurityObject}
     * @throws IllegalArgumentException if name is {@literal null} or an empty String
     */
    public SecurityObject(String name) {
        Assert.hasText(name, "A name of a SecurityObject must not be null");
        this.name = name;
    }

    /**
     * Create a new {@code SecurityObject} with name and description.
     *
     * @param name The name of the {@code SecurityObject}
     * @param description The description text of the {@code SecurityObject}
     * @throws IllegalArgumentException if name is {@literal null} or an empty String
     */
    public SecurityObject(String name, String description) {
        Assert.hasText(name, "A name of a SecurityObject must not be null");
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the name.
     *
     * @return The name of the {@code SecurityObject}
     */
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description text.
     *
     * @return The description of the {@code SecurityObject} as text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description for the {@code SecurityObject}.
     *
     * @param description The description of the {@code SecurityObject} as text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * {@inheritDoc} Compare the name.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SecurityObject other = (SecurityObject) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Return the name.
     *
     * @return the name
     */
    @Override
    public String toString() {
        return name;
    }
}