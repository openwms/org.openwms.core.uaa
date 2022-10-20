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
package org.openwms.core.uaa.impl;

import org.openwms.core.values.CoreTypeDefinitions;
import org.openwms.core.values.ImageProvider;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Detailed information about an {@link User}.
 *
 * @author Heiko Scherrer
 */
@Embeddable
public class UserDetails implements ImageProvider, Serializable {

    /** Some descriptive text of the {@link User}. */
    @Column(name = "C_DESCRIPTION", length = CoreTypeDefinitions.DESCRIPTION_LENGTH)
    private String description;
    /** Some comment text of the {@link User}. */
    @Column(name = "C_COMMENT")
    private String comment;
    /** Phone number assigned to the {@link User}. */
    @Column(name = "C_PHONE_NO")
    private String phoneNo;
    /** IM account assigned to the {@link User}. */
    @Column(name = "C_IM")
    private String im;
    /** Office description assigned to the {@link User}. */
    @Column(name = "C_OFFICE")
    private String office;
    /** Department description assigned to the {@link User}. */
    @Column(name = "C_DEPARTMENT")
    private String department;
    /** An image of the {@link User}. */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "C_IMAGE")
    private byte[] image;
    /** Gender of the {@link User}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "C_GENDER")
    private Gender gender;

    /**
     * The {@link User}s gender.
     *
     * @author Heiko Scherrer
     */
    public enum Gender {
        /** Male. */
        MALE,
        /** Female. */
        FEMALE,
        /** Genderless. */
        GENDERLESS
    }

    /**
     * Return the {@link User}s current phone number.
     *
     * @return The phone number
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Change the phone number of the {@link User}.
     *
     * @param phoneNo The new phone number
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * Supply {@code phoneNo} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyPhoneNo(Consumer<String> c) {
        if (phoneNo != null && !phoneNo.isEmpty()) {
            c.accept(phoneNo);
        }
        return this;
    }
    /**
     * Return the description text of the {@link User}.
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Change the description text of the {@link User}.
     *
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return a comment text of the {@link User}.
     *
     * @return The comment text
     */
    public String getComment() {
        return comment;
    }

    /**
     * Change the comment text of the {@link User}.
     *
     * @param comment The new comment text
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Return the current office of the {@link User}.
     *
     * @return The current office.
     */
    public String getOffice() {
        return office;
    }

    /**
     * Change the current office of the {@link User}.
     *
     * @param office The new office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * Supply {@code office} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyOffice(Consumer<String> c) {
        if (office != null && !office.isEmpty()) {
            c.accept(office);
        }
        return this;
    }

    /**
     * Return the IM account name of the {@link User}.
     *
     * @return The current IM account name
     */
    public String getIm() {
        return im;
    }

    /**
     * Change the current IM account name of the {@link User}.
     *
     * @param im The new IM account name
     */
    public void setIm(String im) {
        this.im = im;
    }

    /**
     * Supply {@code im} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyIm(Consumer<String> c) {
        if (im != null && !im.isEmpty()) {
            c.accept(im);
        }
        return this;
    }

    /**
     * Return the current department of the {@link User}.
     *
     * @return The current department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Change the current department of the {@link User}.
     *
     * @param department The new department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Supply {@code department} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyDepartment(Consumer<String> c) {
        if (department != null && !department.isEmpty()) {
            c.accept(department);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getImage() {
        if (image == null) {
            return new byte[0];
        }
        return Arrays.copyOf(image, image.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImage(byte[] img) {
        image = img == null ? new byte[0] : Arrays.copyOf(img, img.length);
    }

    /**
     * Supply {@code image} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyImage(Consumer<byte[]> c) {
        if (image != null) {
            c.accept(image);
        }
        return this;
    }

    /**
     * Return the {@link User}'s gender.
     *
     * @return The {@link User}'s gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Change the {@link User}'s gender (only for compliance).
     *
     * @param gender The new gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Supply {@code gender} to the consumer {@code c} if present.
     *
     * @param c The consumer
     * @return This instance
     */
    public UserDetails supplyGender(Consumer<Gender> c) {
        if (gender != null) {
            c.accept(gender);
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserDetails.class.getSimpleName() + "[", "]")
                .add("description='" + description + "'")
                .add("comment='" + comment + "'")
                .add("phoneNo='" + phoneNo + "'")
                .add("im='" + im + "'")
                .add("office='" + office + "'")
                .add("department='" + department + "'")
                .add("image=" + Arrays.toString(image))
                .add("gender=" + gender)
                .toString();
    }
}