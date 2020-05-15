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

import java.io.Serializable;
import java.util.Objects;


/**
 * A UserDetailsVO.
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

    private UserDetailsVO(Builder builder) {
        image = builder.image;
        description = builder.description;
        comment = builder.comment;
        phoneNo = builder.phoneNo;
        im = builder.im;
        office = builder.office;
        department = builder.department;
        gender = builder.gender;
    }

    public static Builder newBuilder() {
        return new Builder();
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

    public static final class Builder {
        private String image;
        private String description;
        private String comment;
        private String phoneNo;
        private String im;
        private String office;
        private String department;
        private String gender;

        private Builder() {
        }

        public Builder image(String val) {
            image = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder comment(String val) {
            comment = val;
            return this;
        }

        public Builder phoneNo(String val) {
            phoneNo = val;
            return this;
        }

        public Builder im(String val) {
            im = val;
            return this;
        }

        public Builder office(String val) {
            office = val;
            return this;
        }

        public Builder department(String val) {
            department = val;
            return this;
        }

        public Builder gender(String val) {
            gender = val;
            return this;
        }

        public UserDetailsVO build() {
            return new UserDetailsVO(this);
        }
    }
}
