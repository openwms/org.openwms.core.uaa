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

import org.ameba.annotation.Default;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Role is a group of {@link User}s. Basically more than one {@code User} belong to a Role. Security access policies are assigned to Roles
 * instead of {@link User}s.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see SecurityObject
 * @see User
 */
@Entity
@DiscriminatorValue("ROLE")
public class Role extends SecurityObject implements Serializable {

    public static final String NOT_ALLOWED_TO_CREATE_A_ROLE_WITH_AN_EMPTY_NAME = "Not allowed to create a Role with an empty name";
    /**
     * Whether or not this Role is immutable. Immutable Roles can't be modified. */
    @Column(name = "C_IMMUTABLE")
    private Boolean immutable = false;

    /* ------------------- collection mapping ------------------- */
    /**
     * All {@link User}s assigned to the Role.
     */
    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "COR_UAA_ROLE_USER",
            joinColumns = @JoinColumn(name = "C_ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "C_USER_ID"),
            foreignKey = @ForeignKey(name = "FK_UAA_ROLE_USER"),
            inverseForeignKey = @ForeignKey(name = "FK_UAA_USER_ROLE")
    )
    private Set<User> users = new HashSet<>();

    /**
     * All {@link SecurityObject}s assigned to the Role.
     */
    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "COR_UAA_ROLE_ROLE",
            joinColumns = @JoinColumn(name = "C_ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "C_GRANT_ID"),
            foreignKey = @ForeignKey(name = "FK_UAA_ROLE_GRANT"),
            inverseForeignKey = @ForeignKey(name = "FK_UAA_GRANT_ROLE")
    )
    private Set<SecurityObject> grants = new HashSet<>();

    /** The default prefix String for each created Role. Name is {@value} . */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * A builder class to construct Role instances.
     *
     * @author Heiko Scherrer
     */
    public static class Builder {

        private Role role;

        /**
         * Create a new Builder.
         *
         * @param name The name of the Role
         * @throws IllegalArgumentException when name is {@literal null} or empty
         */
        public Builder(String name) {
            Assert.hasText(name, NOT_ALLOWED_TO_CREATE_A_ROLE_WITH_AN_EMPTY_NAME);
            role = new Role(name);
            role.immutable = false;
        }

        /**
         * Add a description text to the Role.
         *
         * @param description as String
         * @return the builder instance
         */
        public Builder withDescription(String description) {
            role.setDescription(description);
            return this;
        }

        /**
         * Set the Role to be immutable.
         *
         * @return the builder instance
         */
        public Builder asImmutable() {
            role.immutable = true;
            return this;
        }

        /**
         * Finally build and return the Role instance.
         *
         * @return the constructed Role
         */
        public Role build() {
            return role;
        }
    }

    /* ----------------------------- methods ------------------- */

    /** Dear JPA... */
    protected Role() { }

    /**
     * Create a new Role with a name.
     *
     * @param name The name of the Role
     * @throws IllegalArgumentException when name is {@literal null} or empty
     */
    @Default
    public Role(String name) {
        Assert.hasText(name, NOT_ALLOWED_TO_CREATE_A_ROLE_WITH_AN_EMPTY_NAME);
        setName(normalizeName(name));
    }

    public String normalizeName(String name) {
        if (name != null && !name.startsWith(ROLE_PREFIX)) {
            return ROLE_PREFIX + name;
        }
        return name;
    }

    /**
     * Create a new Role with a name and a description.
     *
     * @param name The name of the Role
     * @param description The description text of the Role
     * @throws IllegalArgumentException when name is {@literal null} or empty
     */
    public Role(String name, String description) {
        Assert.hasText(name, NOT_ALLOWED_TO_CREATE_A_ROLE_WITH_AN_EMPTY_NAME);
        setName(normalizeName(name));
        setDescription(description);
    }

    @Override
    public void setPersistentKey(String pKey) {
        super.setPersistentKey(pKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setName(String name) {
        super.setName(normalizeName(name));
    }

    /**
     * Get the immutable.
     *
     * @return the immutable.
     */
    public Boolean getImmutable() {
        return immutable;
    }

    /**
     * Return a Set of all {@link User}s assigned to the Role.
     *
     * @return A Set of all {@link User}s assigned to the Role
     */
    public Set<User> getUsers() {
        if (users == null) {
            return new HashSet<>();
        }
        return new HashSet<>(users);
    }

    /**
     * Add an existing {@link User} to the Role.
     *
     * @param user The {@link User} to be added
     * @return {@literal true} if the {@link User} was new in the collection of {@link User}s, otherwise {@literal false}
     * @throws IllegalArgumentException if user is {@literal null}
     */
    public boolean addUser(User user) {
        Assert.notNull(user, "User to add must not be null");
        return users.add(user);
    }

    /**
     * Remove a {@link User} from the Role.
     *
     * @param user The {@link User} to be removed
     * @throws IllegalArgumentException if user is {@literal null}
     */
    public void removeUser(User user) {
        Assert.notNull(user, "User to remove must not be null");
        users.remove(user);
    }

    /**
     * Set all {@link User}s belonging to this Role.
     *
     * @param users A Set of {@link User}s to be assigned to the Role
     * @throws IllegalArgumentException if users is {@literal null}
     */
    public void setUsers(Set<User> users) {
        Assert.notNull(users, "Set of Users must not be null");
        this.users = users;
    }

    /**
     * Return a Set of all {@link SecurityObject}s belonging to the Role.
     *
     * @return A Set of all {@link SecurityObject}s belonging to this Role
     */
    public Set<SecurityObject> getGrants() {
        if (grants == null) {
            grants = new HashSet<>();
        }
        return new HashSet<>(grants);
    }

    /**
     * Add an existing {@link SecurityObject} to the Role.
     *
     * @param grant The {@link SecurityObject} to be added to the Role.
     * @return {@literal true} if the {@link SecurityObject} was new to the collection of {@link SecurityObject}s, otherwise {@literal
     * false}
     * @throws IllegalArgumentException if grant is {@literal null}
     */
    public boolean addGrant(SecurityObject grant) {
        Assert.notNull(grant, "Grant to add must not be null");
        return grants.add(grant);
    }

    /**
     * Add an existing {@link SecurityObject} to the Role.
     *
     * @param grant The {@link SecurityObject} to be added to the Role
     * @return {@literal true} if the {@link SecurityObject} was successfully removed from the Set of {@link SecurityObject}s, otherwise
     * {@literal false}
     * @throws IllegalArgumentException if grant is {@literal null}
     */
    public boolean removeGrant(SecurityObject grant) {
        Assert.notNull(grant, "Grant to remove must not be null");
        return grants.remove(grant);
    }

    /**
     * Add an existing {@link SecurityObject} to the Role.
     *
     * @param grants A list of {@link SecurityObject}s to be removed from the Role
     * @return {@literal true} if the {@link SecurityObject} was successfully removed from the Set of {@link SecurityObject}s, otherwise
     * {@literal false}
     * @throws IllegalArgumentException if {@code grants} is {@literal null}
     */
    public boolean removeGrants(List<? extends SecurityObject> grants) {
        Assert.notNull(grants, "Grants to remove must not be null");
        return this.grants.removeAll(grants);
    }

    /**
     * Set all {@link SecurityObject}s assigned to the Role. Already existing {@link SecurityObject}s will be removed.
     *
     * @param grants A Set of {@link SecurityObject}s to be assigned to the Role
     * @throws IllegalArgumentException if grants is {@literal null}
     */
    public void setGrants(Set<SecurityObject> grants) {
        Assert.notNull(grants, "Set of Grants must not be null");
        this.grants = grants;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates to the superclass and uses the hashCode of the String ROLE for calculation.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = super.hashCode();
        result = prime * result + "ROLE".hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Does not delegate to the {@link SecurityObject#equals(Object)} and uses the name for comparison.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Role)) {
            return false;
        }
        Role other = (Role) obj;
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