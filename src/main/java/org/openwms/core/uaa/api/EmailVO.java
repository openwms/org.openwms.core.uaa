/*
 * Copyright 2005-2021 the original author or authors.
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

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * A EmailVO.
 *
 * @author Heiko Scherrer
 */
public class EmailVO implements Serializable {

    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("primary")
    private Boolean primary;

    public EmailVO() {
    }

    public EmailVO(String emailAddress, Boolean primary) {
        this.emailAddress = emailAddress;
        this.primary = primary;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailVO)) return false;
        EmailVO emailVO = (EmailVO) o;
        return Objects.equals(emailAddress, emailVO.emailAddress) && Objects.equals(primary, emailVO.primary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, primary);
    }
}
