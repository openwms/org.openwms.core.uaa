/*
 * Copyright 2005-2022 the original author or authors.
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EmailVO represents an email.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailVO implements Serializable {

    /** The email address as String (not blank). */
    @JsonProperty("emailAddress")
    @NotBlank
    private String emailAddress;
    /** Whether this email address is the primary email used in the system. */
    @JsonProperty("primary")
    private Boolean primary;
    /** The fullname of the {@code User}. */
    @JsonProperty("fullname")
    private String fullname;

    /*~-------------------- constructors --------------------*/
    @ConstructorProperties({"emailAddress", "primary"})
    public EmailVO(@NotBlank String emailAddress, boolean primary) {
        this.emailAddress = emailAddress;
        this.primary = primary;
    }

    /*~-------------------- accessors --------------------*/
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
        if (!(o instanceof EmailVO)) return false;
        EmailVO emailVO = (EmailVO) o;
        return Objects.equals(emailAddress, emailVO.emailAddress) && Objects.equals(primary, emailVO.primary) && Objects.equals(fullname, emailVO.fullname);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, primary, fullname);
    }

    /**
     * {@inheritDoc}
     *
     * Only emailAddress.
     */
    @Override
    public String toString() {
        return emailAddress;
    }
}
