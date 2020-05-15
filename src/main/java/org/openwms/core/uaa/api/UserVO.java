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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * A UserVO.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO extends AbstractBase implements Serializable {

    @JsonProperty("pKey")
    private String pKey;
    @JsonProperty("username")
    @NotEmpty(groups = ValidationGroups.Create.class)
    private String username;
    @JsonProperty("externalUser")
    private Boolean extern;
    @JsonProperty("lastPasswordChange")
    private Date lastPasswordChange;
    @JsonProperty("locked")
    private Boolean locked;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("expirationDate")
    private Date expirationDate;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("details")
    @Valid
    private UserDetailsVO userDetails = new UserDetailsVO();
    @JsonProperty("email")
    @NotEmpty(groups = ValidationGroups.Create.class)
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

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Date getExpirationDate() {
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

    public static final class Builder {
        private String pKey;
        private @NotEmpty(groups = ValidationGroups.Create.class) String username;
        private Boolean extern;
        private Date lastPasswordChange;
        private Boolean locked;
        private Boolean enabled;
        private Date expirationDate;
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

        public Builder lastPasswordChange(Date val) {
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

        public Builder expirationDate(Date val) {
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
