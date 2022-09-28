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
public class GrantVO extends SecurityObjectVO<GrantVO> implements Serializable {

    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = "application/vnd.openwms.uaa.grant-v1+json";

    /** The persistent key. */
    @JsonProperty("pKey")
    private String pKey;
    /** Unique name of the Role. */
    @JsonProperty("name")
    @NotEmpty(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private String name;
    /** Whether or not this Role is immutable. Immutable Roles can't be modified. */
    @JsonProperty("immutable")
    private Boolean immutable;
    /** A descriptive text for the Role. */
    @JsonProperty("description")
    private String description;
    /** All Users assigned to the Role. */
    @JsonProperty("users")
    private Set<UserVO> users = new HashSet<>();
    /** A collection of Grants that are assigned to the Role. */
    @JsonProperty("grants")
    private Set<SecurityObjectVO> grants = new HashSet<>();

    @JsonCreator
    public GrantVO() { }

    private GrantVO(Builder builder) {
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
        GrantVO roleVO = (GrantVO) o;
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

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return "RoleVO{" +
                "pKey='" + pKey + '\'' +
                ", name='" + name + '\'' +
                ", immutable=" + immutable +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", grants=" + grants +
                '}';
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

        public GrantVO build() {
            return new GrantVO(this);
        }
    }
}
