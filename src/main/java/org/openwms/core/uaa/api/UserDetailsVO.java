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

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;


/**
 * A UserDetailsVO encapsulates less relevant details of an User.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsVO implements Serializable {

    /** An base64 encoded image file. */
    @JsonProperty("image")
    private String image;
    /** Some descriptive text about the User. */
    @JsonProperty("description")
    private String description;
    /** Any comment text added to the User. */
    @JsonProperty("comment")
    private String comment;
    /** The User's phone number. */
    @JsonProperty("phoneNo")
    private String phoneNo;
    /** The User's messenger name. */
    @JsonProperty("im")
    private String im;
    /** The User's office. */
    @JsonProperty("office")
    private String office;
    /** The User's department. */
    @JsonProperty("department")
    private String department;
    /** The User's gender. */
    @JsonProperty("gender")
    private String gender;

    @JsonCreator
    public UserDetailsVO() {
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getComment() {
        return comment;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getIm() {
        return im;
    }

    public String getOffice() {
        return office;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
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
        UserDetailsVO that = (UserDetailsVO) o;
        return Objects.equals(image, that.image) &&
                Objects.equals(description, that.description) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(im, that.im) &&
                Objects.equals(office, that.office) &&
                Objects.equals(department, that.department) &&
                Objects.equals(gender, that.gender);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(image, description, comment, phoneNo, im, office, department, gender);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", UserDetailsVO.class.getSimpleName() + "[", "]")
                .add("image='" + image + "'")
                .add("description='" + description + "'")
                .add("comment='" + comment + "'")
                .add("phoneNo='" + phoneNo + "'")
                .add("im='" + im + "'")
                .add("office='" + office + "'")
                .add("department='" + department + "'")
                .add("gender='" + gender + "'")
                .toString();
    }
}
