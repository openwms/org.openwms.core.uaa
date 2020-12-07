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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ameba.http.AbstractBase;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A RoleVO is the representation of the role an User is assigned to.
 * 
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleVO extends AbstractBase implements Serializable {

    /** The persistent key. */
    @JsonProperty("pKey")
    @Null(groups = {ValidationGroups.Create.class})
    private String pKey;
    /** The unique Role name. */
    @JsonProperty("name")
    @NotEmpty(groups = {ValidationGroups.Create.class})
    private String name;
    /** If the Role can be modified. */
    @JsonProperty("immutable")
    private Boolean immutable;
    /** A descriptive text for the Role. */
    @JsonProperty("description")
    private String description;
    /** A collection of Users that are assigned to the Role. */
    @JsonProperty("users")
    @Null(groups = {ValidationGroups.Create.class})
    private Set<UserVO> users = new HashSet<>();
    /** A collection of Grants that are assigned to the Role. */
    @JsonProperty("grants")
    @Null(groups = {ValidationGroups.Create.class})
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
