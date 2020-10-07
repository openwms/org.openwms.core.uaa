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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ameba.http.AbstractBase;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A RoleVO is the representation of the role an User is assigned to.
 * 
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleVO extends AbstractBase implements Serializable {

    /** The persistent key. */
    @JsonProperty("pKey")
    private String pKey;
    /** The unique Role name. */
    @JsonProperty("name")
    @NotEmpty
    private String name;
    /** If the Role can be modified. */
    @JsonProperty("immutable")
    private Boolean immutable;
    /** A descriptive text for the Role. */
    @JsonProperty("description")
    private String description;
    /** A collection of Users that are assigned to the Role. */
    @JsonProperty("users")
    private Set<UserVO> users = new HashSet<>();
    /** A collection of Grants that are assigned to the Role. */
    @JsonProperty("grants")
    private Set<SecurityObjectVO> grants = new HashSet<>();

    @JsonCreator
    public RoleVO() {
    }

    private RoleVO(Builder builder) {
        pKey = builder.pKey;
        name = builder.name;
        immutable = builder.immutable;
        description = builder.description;
        users = builder.users;
        grants = builder.grants;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getpKey() {
        return pKey;
    }

    public String getName() {
        return name;
    }

    public Boolean getImmutable() {
        return immutable;
    }

    public String getDescription() {
        return description;
    }

    public Set<UserVO> getUsers() {
        return users;
    }

    public Set<SecurityObjectVO> getGrants() {
        return grants;
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoleVO roleVO = (RoleVO) o;
        return Objects.equals(pKey, roleVO.pKey) &&
                Objects.equals(name, roleVO.name) &&
                Objects.equals(immutable, roleVO.immutable) &&
                Objects.equals(description, roleVO.description) &&
                Objects.equals(users, roleVO.users) &&
                Objects.equals(grants, roleVO.grants);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, name, immutable, description, users, grants);
    }

    public static final class Builder {
        private String pKey;
        private String name;
        private Boolean immutable;
        private String description;
        private Set<UserVO> users;
        private Set<SecurityObjectVO> grants;

        private Builder() {
        }

        public Builder pKey(String val) {
            pKey = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder immutable(Boolean val) {
            immutable = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder users(Set<UserVO> val) {
            users = val;
            return this;
        }

        public Builder grants(Set<SecurityObjectVO> val) {
            grants = val;
            return this;
        }

        public RoleVO build() {
            return new RoleVO(this);
        }
    }
}
