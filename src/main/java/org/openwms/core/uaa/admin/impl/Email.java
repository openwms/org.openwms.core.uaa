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
package org.openwms.core.uaa.admin.impl;

import org.ameba.integration.jpa.BaseEntity;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Objects;

/**
 * An Email represents the email address of an {@code User}.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see User
 */
@Entity
@Table(name = "COR_UAA_EMAIL",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_EMAIL_USER", columnNames = {"C_USERNAME", "C_ADDRESS"})
})
public class Email extends BaseEntity implements Serializable {

    /** Unique identifier of the {@code Email} (not nullable). */
    @ManyToOne
    @JoinColumn(name = "C_USERNAME", referencedColumnName = "C_USERNAME", foreignKey = @ForeignKey(name = "FK_UAA_USER_EMAIL"))
    private User user;
    /** The email address as String (not nullable). */
    @Column(name = "C_ADDRESS", nullable = false)
    private String emailAddress;
    /** Whether this email address is the primary email used in the system. */
    @Column(name = "C_PRIMARY", nullable = false)
    private boolean primary = false;
    /** The fullname of the {@code User}. */
    @Column(name = "C_FULL_NAME")
    private String fullname;

    /* ----------------------------- methods ------------------- */

    /**
     * Dear JPA...
     */
    protected Email() {
    }

    /**
     * Create a new {@code Email}.
     *
     * @param user The User
     * @param emailAddress The email address of the User
     * @throws IllegalArgumentException when userName or emailAddress is {@literal null} or empty
     */
    public Email(User user, String emailAddress) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(emailAddress, "EmailAddress must not be null or empty");
        this.user = user;
        this.emailAddress = emailAddress;
    }

    /**
     * Create a new {@code Email}.
     *
     * @param user The User
     * @param emailAddress The email address of the User
     * @param primary If the email is the primary address
     * @throws IllegalArgumentException when userName or emailAddress is {@literal null} or empty
     */
    public Email(User user, String emailAddress, boolean primary) {
        this(user, emailAddress);
        this.primary = primary;
    }

    /**
     * Returns the {@code User}.
     *
     * @return The User
     */
    public User getUser() {
        return user;
    }

    /**
     * Return the emailAddress.
     *
     * @return The emailAddress.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the emailAddress.
     *
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Return the fullname.
     *
     * @return The fullname.
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Set the fullname.
     *
     * @param fullname The fullname to set.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
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
        Email email = (Email) o;
        return Objects.equals(user, email.user) &&
                Objects.equals(emailAddress, email.emailAddress) &&
                Objects.equals(fullname, email.fullname);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, emailAddress, fullname);
    }

    /**
     * Return the emailAddress as String.
     *
     * @return the emailAddress
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return emailAddress;
    }
}