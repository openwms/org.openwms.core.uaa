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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A GrantVO is the representation of a permission an User is granted to.
 * 
 * @author Heiko Scherrer
 */
public class GrantVO extends SecurityObjectVO<GrantVO> implements Serializable {

    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = "application/vnd.openwms.uaa.grant-v1+json";

    @JsonCreator
    public GrantVO() {
        // For Jackson and MapStruct usage
    }

    public static final class Builder {
        private String pKey;
        private @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class}) String name;
        private String description;

        private Builder() {
        }

        public static Builder aGrantVO() {
            return new Builder();
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

        public GrantVO build() {
            GrantVO grantVO = new GrantVO();
            grantVO.setName(name);
            grantVO.setDescription(description);
            grantVO.setpKey(this.pKey);
            return grantVO;
        }
    }
}
