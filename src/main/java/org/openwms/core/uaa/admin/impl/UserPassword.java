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
package org.openwms.core.uaa.admin.impl;

import org.ameba.integration.jpa.BaseEntity;
import org.openwms.core.uaa.app.DefaultTimeProvider;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Is a representation of an {@link User} together with her password. <p> When an {@link User} changes her password, the current password is
 * added to a history list of passwords. This is necessary to omit {@link User}s from setting formerly used passwords. </p>
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see User
 */
@Entity
@Table(name = "COR_UAA_USER_PASSWORD")
public class UserPassword extends BaseEntity implements Serializable {

    /** {@link User} assigned to this password. */
    @ManyToOne
    @JoinColumn(name = "C_USERNAME", referencedColumnName = "C_USERNAME", foreignKey = @ForeignKey(name = "FK_UAA_PW_USER"))
    private User user;
    /** Password. */
    @Column(name = "C_PASSWORD")
    private String password;
    /** Date of the last password change. */
    @Column(name = "C_PASSWORD_CHANGED")
    @OrderBy
    private ZonedDateTime passwordChanged = new DefaultTimeProvider().nowAsZonedDateTime();

    /* ----------------------------- methods ------------------- */

    /**
     * Create a new {@link UserPassword}.
     *
     * @param user The {@link User} to assign
     * @param password The {@code password} as String to assign
     * @throws IllegalArgumentException when {@link User} or {@code password} is {@literal null} or empty
     */
    public UserPassword(User user, String password) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(password, "Password must not be null");
        this.user = user;
        this.password = password;
    }

    /**
     * Dear JPA...
     */
    protected UserPassword() {
        super();
    }

    /**
     * Return the {@link User} of this password.
     *
     * @return The {@link User} of this password
     */
    public User getUser() {
        return user;
    }

    /**
     * Change the {@link User}.
     *
     * @param user The new {@link User}
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return the current password.
     *
     * @return The current password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Return the date of the last password change.
     *
     * @return The date when the password has changed
     */
    public ZonedDateTime getPasswordChanged() {
        return passwordChanged;
    }

    @Override
    public String toString() {
        return "*******";
    }

    /**
     * {@inheritDoc}
     * <p>
     * Does not call the superclass. Uses the password and user for calculation.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Comparison is done with the business-key (user and password).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserPassword)) {
            return false;
        }
        var other = (UserPassword) obj;
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }
}