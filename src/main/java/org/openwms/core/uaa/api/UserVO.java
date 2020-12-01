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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A UserVO is the representation of a human User with all attributes of interest.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO extends AbstractBase implements Serializable {

    /** The persistent key. */
    @JsonProperty("pKey")
    private String pKey;
    /** The User's unique name. */
    @JsonProperty("username")
    @NotEmpty(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private String username;
    /** If the User is authenticated against an external system. */
    @JsonProperty("externalUser")
    private Boolean extern;
    /** When the password has been changed the last time. */
    @JsonProperty("lastPasswordChange")
    @Null(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private ZonedDateTime lastPasswordChange;
    /** If the User is locked from login. */
    @JsonProperty("locked")
    private Boolean locked;
    /** If the User is generally enabled in the system. */
    @JsonProperty("enabled")
    private Boolean enabled;
    /** When the password expires. */
    @JsonProperty("expirationDate")
    private ZonedDateTime expirationDate;
    /** The Users full name. */
    @JsonProperty("fullname")
    private String fullname;
    /** More specific details of the User. */
    @JsonProperty("details")
    @Valid
    private UserDetailsVO userDetails;
    /** The User's email address. */
    @JsonProperty("email")
    @NotEmpty(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private String email;

    @JsonCreator
    public UserVO() {
    }

    private UserVO(Builder builder) {
        pKey = builder.pKey;
        username = builder.username;
        extern = builder.extern;
        lastPasswordChange = builder.lastPasswordChange;
        locked = builder.locked;
        enabled = builder.enabled;
        expirationDate = builder.expirationDate;
        fullname = builder.fullname;
        userDetails = builder.userDetails;
        email = builder.email;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getpKey() {
        return pKey;
    }

    public Boolean getExtern() {
        return extern;
    }

    public String getUsername() {
        return username;
    }

    public ZonedDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public String getFullname() {
        return fullname;
    }

    public UserDetailsVO getUserDetails() {
        return userDetails;
    }

    public String getEmail() {
        return email;
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
        UserVO userVO = (UserVO) o;
        return Objects.equals(pKey, userVO.pKey) &&
                Objects.equals(username, userVO.username) &&
                Objects.equals(extern, userVO.extern) &&
                Objects.equals(lastPasswordChange, userVO.lastPasswordChange) &&
                Objects.equals(locked, userVO.locked) &&
                Objects.equals(enabled, userVO.enabled) &&
                Objects.equals(expirationDate, userVO.expirationDate) &&
                Objects.equals(fullname, userVO.fullname) &&
                Objects.equals(userDetails, userVO.userDetails) &&
                Objects.equals(email, userVO.email);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, username, extern, lastPasswordChange, locked, enabled, expirationDate, fullname, userDetails, email);
    }

    public void setpKey(java.lang.String pKey) {
        this.pKey = pKey;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public void setExtern(java.lang.Boolean extern) {
        this.extern = extern;
    }

    public void setLastPasswordChange(ZonedDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public void setLocked(java.lang.Boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(java.lang.Boolean enabled) {
        this.enabled = enabled;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setFullname(java.lang.String fullname) {
        this.fullname = fullname;
    }

    public void setUserDetails(UserDetailsVO userDetails) {
        this.userDetails = userDetails;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public static final class Builder {
        private String pKey;
        private @NotEmpty(groups = ValidationGroups.Create.class) String username;
        private Boolean extern;
        private ZonedDateTime lastPasswordChange;
        private Boolean locked;
        private Boolean enabled;
        private ZonedDateTime expirationDate;
        private String fullname;
        private @Valid UserDetailsVO userDetails;
        private @NotEmpty(groups = ValidationGroups.Create.class) String email;

        private Builder() {
        }

        public Builder pKey(String val) {
            pKey = val;
            return this;
        }

        public Builder username(@NotEmpty(groups = ValidationGroups.Create.class) String val) {
            username = val;
            return this;
        }

        public Builder extern(Boolean val) {
            extern = val;
            return this;
        }

        public Builder lastPasswordChange(ZonedDateTime val) {
            lastPasswordChange = val;
            return this;
        }

        public Builder locked(Boolean val) {
            locked = val;
            return this;
        }

        public Builder enabled(Boolean val) {
            enabled = val;
            return this;
        }

        public Builder expirationDate(ZonedDateTime val) {
            expirationDate = val;
            return this;
        }

        public Builder fullname(String val) {
            fullname = val;
            return this;
        }

        public Builder userDetails(@Valid UserDetailsVO val) {
            userDetails = val;
            return this;
        }

        public Builder email(@NotEmpty(groups = ValidationGroups.Create.class) String val) {
            email = val;
            return this;
        }

        public UserVO build() {
            return new UserVO(this);
        }
    }
}
