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

/**
 * Detailed information about an {@link User}.
 *
 * @author Heiko Scherrer
 * @version 0.2
 * @see User
 * @since 0.1
 */
@Embeddable
public class UserDetails implements ImageProvider, Serializable {

    /**
     * Some descriptive text of the {@link User}.
     */
    @Column(name = "C_DESCRIPTION", length = CoreTypeDefinitions.DESCRIPTION_LENGTH)
    private String description;
    /**
     * Some comment text of the {@link User}.
     */
    @Column(name = "C_COMMENT")
    private String comment;
    /**
     * Phone number assigned to the {@link User}.
     */
    @Column(name = "C_PHONE_NO")
    private String phoneNo;
    /**
     * IM account assigned to the {@link User}.
     */
    @Column(name = "C_IM")
    private String skypeName;
    /**
     * Office description assigned to the {@link User}.
     */
    @Column(name = "C_OFFICE")
    private String office;
    /**
     * Department description assigned to the {@link User}.
     */
    @Column(name = "C_DEPARTMENT")
    private String department;
    /**
     * An image of the {@link User}. Lazy fetched.
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "C_IMAGE")
    private byte[] image;
    /**
     * Sex of the {@link User}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "C_SEX")
    private SEX sex;

    /**
     * The {@link User}s sex.
     *
     * @author Heiko Scherrer
     * @version 0.2
     * @see User
     * @since 0.1
     */
    public static enum SEX {
        /**
         * Male.
         */
        MALE,
        /**
         * Female.
         */
        FEMALE
    }

    /* ----------------------------- methods ------------------- */

    /**
     * Create a new {@link UserDetails} instance.
     */
    public UserDetails() {
        super();
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
     * Return the IM account name of the {@link User}.
     *
     * @return The current IM account name
     */
    public String getSkypeName() {
        return skypeName;
    }

    /**
     * Change the current IM account name of the {@link User}.
     *
     * @param skypeName The new IM account name
     */
    public void setSkypeName(String skypeName) {
        this.skypeName = skypeName;
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
     * Return the {@link User}'s sex.
     *
     * @return The {@link User}'s sex
     */
    public SEX getSex() {
        return sex;
    }

    /**
     * Change the {@link User}'s sex (only for compliance).
     *
     * @param sex The new sex
     */
    public void setSex(SEX sex) {
        this.sex = sex;
    }
}