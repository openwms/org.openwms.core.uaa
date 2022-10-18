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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A RoleVO is groups a collections of permissions (grants). Users might be assigned to roles.
 * 
 * @author Heiko Scherrer
 */
public class RoleVO extends SecurityObjectVO<RoleVO> implements Serializable {

    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = "application/vnd.openwms.uaa.role-v1+json";

    /** Whether this Role is immutable or not. Immutable Roles can't be modified. */
    @JsonProperty("immutable")
    private Boolean immutable;
    /** All Users assigned to the Role. */
    @JsonProperty("users")
    private Set<UserVO> users = new HashSet<>();
    /** A collection of Grants that are assigned to the Role. */
    @JsonProperty("grants")
    private Set<SecurityObjectVO> grants = new HashSet<>();

    /*~-------------------- constructors --------------------*/
    @JsonCreator
    public RoleVO() {
        // NOT for the mapper
    }

    private RoleVO(Builder builder) {
        this.setName(builder.name);
        this.setDescription(builder.description);
        this.immutable = builder.immutable;
        this.users = builder.users;
        this.setpKey(builder.pKey);
        this.grants = builder.grants;
        super.setOl(builder.ol);
    }

    /* Used by the mapper. */
    public static RoleVO.Builder newBuilder() {
        return new RoleVO.Builder();
    }

    /*~-------------------- accessors --------------------*/
    public Boolean getImmutable() {
        return immutable;
    }

    public Set<UserVO> getUsers() {
        return users;
    }

    public Set<SecurityObjectVO> getGrants() {
        return grants;
    }

    /*~-------------------- methods --------------------*/
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
        var roleVO = (RoleVO) o;
        return Objects.equals(immutable, roleVO.immutable) &&
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
        return Objects.hash(super.hashCode(), immutable, users, grants);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return "RoleVO{" +
                "immutable=" + immutable +
                ", users=" + users +
                ", grants=" + grants +
                '}';
    }

    /*~-------------------- builder --------------------*/
    public static final class Builder {
        private Boolean immutable;
        private Set<UserVO> users;
        private Set<SecurityObjectVO> grants;
        private String pKey;
        private String name;
        private String description;
        private long ol;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder immutable(Boolean immutable) {
            this.immutable = immutable;
            return this;
        }

        public Builder users(Set<UserVO> users) {
            this.users = users;
            return this;
        }

        public Builder grants(Set<SecurityObjectVO> grants) {
            this.grants = grants;
            return this;
        }

        public Builder pKey(String pKey) {
            this.pKey = pKey;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder ol(long val) {
            ol = val;
            return this;
        }

        public RoleVO build() {
            return new RoleVO(this);
        }
    }
}
