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

import org.ameba.integration.jpa.ApplicationEntity;
import org.openwms.core.uaa.app.DefaultTimeProvider;
import org.openwms.core.exception.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * An User represents a human user of the system. Typically an User is assigned to one or more {@code Roles} to define security constraints.
 * Users can have their own configuration settings in form of {@code UserPreferences} and certain user details, encapsulated in an {@code
 * UserDetails} object that tend to be extended by projects.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see UserDetails
 * @see UserPassword
 * @see Role
 */
@Entity
@Table(name = "COR_UAA_USER", uniqueConstraints = @UniqueConstraint(name = "UC_UAA_USER_NAME", columnNames = {"C_USERNAME"}))
@Inheritance
@DiscriminatorColumn(name = "C_TYPE")
@DiscriminatorValue("STANDARD")
public class User extends ApplicationEntity implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
    /** Unique identifier of this User (not nullable). */
    @Column(name = "C_USERNAME", nullable = false)
    @NotEmpty
    private String username;
    /** {@code true} if the User is authenticated by an external system, otherwise {@code false}. */
    @Column(name = "C_EXTERN")
    private boolean extern = false;
    /** Date of the last password change. */
    @Column(name = "C_LAST_PASSWORD_CHANGE")
    private ZonedDateTime lastPasswordChange;
    /** {@code true} if this User is locked and has no permission to login. */
    @Column(name = "C_LOCKED")
    private boolean locked = false;
    /** The User's current password (only kept transient). */
    @Transient
    private String password;
    /** The User's current password. */
    @Column(name = "C_PASSWORD")
    private String persistedPassword;
    /** {@code true} if the User is enabled. This field can be managed by the UI application to lock the User manually. */
    @Column(name = "C_ENABLED")
    private boolean enabled = true;
    /** Date when the account expires. After account expiration, the User cannot login anymore. */
    @Column(name = "C_EXPIRATION_DATE")
    private ZonedDateTime expirationDate;
    /** The User's fullname (doesn't have to be unique). */
    @Column(name = "C_FULLNAME")
    private String fullname;
    /** Email addresses. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emailAddresses;
    /** More detail information of the User. */
    @Embedded
    private UserDetails userDetails;
    /** List of {@link Role}s assigned to the User. */
    @ManyToMany(mappedBy = "users", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();
    /** Last passwords of the User. */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<UserPassword> passwords = new ArrayList<>();
    /** The number of passwords to keep in the password history. Default: {@value}. */
    public static final short NUMBER_STORED_PASSWORDS = 3;

    /* ----------------------------- constructors ------------------- */

    /** Dear JPA... */
    protected User() {
        super();
        loadLazy();
    }

    /**
     * Create a new User with an username.
     *
     * @param username The unique name of the user
     * @throws IllegalArgumentException when username is {@literal null} or empty
     */
    public User(String username) {
        super();
        Assert.hasText(username, "Not allowed to create an User with an empty username");
        this.username = username;
        loadLazy();
    }

    /**
     * Create a new User with an username.
     *
     * @param username The unique name of the user
     * @param password The password of the user
     * @throws IllegalArgumentException when username or password is {@literal null} or empty
     */
    protected User(String username, String password) {
        super();
        Assert.hasText(username, "Not allowed to create an User with an empty username");
        Assert.hasText(password, "Not allowed to create an User with an empty password");
        this.username = username;
        this.password = password;
    }

    /* ----------------------------- methods ------------------- */

    /**
     * After load, the saved password is copied to the transient one. The transient one can be overridden by the application to force a
     * password change.
     */
    @PostLoad
    public void postLoad() {
        loadLazy();
    }

    protected void loadLazy() {
        password = persistedPassword;
    }

    /**
     * Return the unique username of the User.
     *
     * @return The current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change the username of the User.
     *
     * @param username The new username to set
     */
    protected void setUsername(String username) {
        this.username = username;
    }

    /**
     * Is the User authenticated by an external system?
     *
     * @return {@literal true} if so, otherwise {@literal false}
     */
    public boolean isExternalUser() {
        return extern;
    }

    /**
     * Change the authentication method of the User.
     *
     * @param externalUser {@literal true} if the User was authenticated by an external system, otherwise {@literal false}.
     */
    public void setExternalUser(boolean externalUser) {
        extern = externalUser;
    }

    /**
     * Return the date when the password has changed recently.
     *
     * @return The date when the password has changed recently
     */
    public ZonedDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    /**
     * Check if the User is locked.
     *
     * @return {@literal true} if locked, otherwise {@literal false}
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Lock the User.
     *
     * @param locked {@literal true} to lock the User, {@literal false} to unlock
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Returns the current password of the User.
     *
     * @return The current password as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks if the new password is a valid and change the password of this User.
     *
     * @param encodedPassword The new encoded password of this User
     * @throws InvalidPasswordException in case changing the password is not allowed or the new password is not valid
     */
    public void changePassword(String encodedPassword, String rawPassword, PasswordEncoder encoder) throws InvalidPasswordException {
        if (persistedPassword != null && encoder.matches(rawPassword, persistedPassword)) {
            return;
        }
        validateAgainstPasswordHistory(rawPassword, encoder);
        storeOldPassword(password);
        persistedPassword = encodedPassword;
        password = encodedPassword;
        lastPasswordChange = new DefaultTimeProvider().nowAsZonedDateTime();
    }

    /**
     * Checks whether the password is going to change.
     *
     * @return {@literal true} when {@code password} is different to the originally persisted one, otherwise {@literal false}
     */
    public boolean hasPasswordChanged() {
        return (persistedPassword.equals(password));
    }

    /**
     * Check whether the new password is in the history of former passwords.
     *
     * @param rawPassword The password to verify
     */
    protected void validateAgainstPasswordHistory(String rawPassword, PasswordEncoder encoder) throws InvalidPasswordException {
        for (UserPassword up : passwords) {
            if (encoder.matches(rawPassword, up.getPassword())) {
                throw new InvalidPasswordException("Password does not match the defined rules");
            }
        }
    }

    private void storeOldPassword(String oldPassword) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("If the old password is null, do not store it in history");
            }
            return;
        }
        passwords.add(new UserPassword(this, oldPassword));
        if (passwords.size() > NUMBER_STORED_PASSWORDS) {
            Collections.sort(passwords, new PasswordComparator());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Remove the old password from the history: [{}]", (passwords.get(passwords.size() - 1)));
            }
            UserPassword pw = passwords.get(passwords.size() - 1);
            pw.setUser(null);
            passwords.remove(pw);
        }
    }

    /**
     * Determines whether the User is enabled or not.
     *
     * @return {@literal true} if the User is enabled, otherwise {@literal false}
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable the User.
     *
     * @param enabled {@literal true} when enabled, otherwise {@literal false}
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Return the date when the account expires.
     *
     * @return The expiration date
     */
    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Change the date when the account expires.
     *
     * @param expDate The new expiration date to set
     */
    public void setExpirationDate(ZonedDateTime expDate) {
        expirationDate = expDate;
    }

    /**
     * Returns a list of granted {@link Role}s.
     *
     * @return The list of granted {@link Role}s
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Flatten {@link Role}s and {@link Grant}s and return an unmodifiable list of all {@link Grant}s assigned to this User.
     *
     * @return A list of all {@link Grant}s
     */
    public List<SecurityObject> getGrants() {
        List<SecurityObject> grants = new ArrayList<>();
        for (Role role : getRoles()) {
            grants.addAll(role.getGrants());
        }
        return Collections.unmodifiableList(grants);
    }

    /**
     * Add a new {@link Role} to the list of {@link Role}s.
     *
     * @param role The new {@link Role} to add
     * @return see {@link java.util.Collection#add(Object)}
     */
    public boolean addRole(Role role) {
        return roles.add(role);
    }

    /**
     * Set the {@link Role}s of this User. Existing {@link Role}s will be overridden.
     *
     * @param roles The new list of {@link Role}s
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Return the fullname of the User.
     *
     * @return The current fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Change the fullname of the User.
     *
     * @param fullname The new fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Set<Email> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<Email> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    /**
     * Return a list of recently used passwords.
     *
     * @return A list of recently used passwords
     */
    public List<UserPassword> getPasswords() {
        return passwords;
    }

    /**
     * Return the details of the User.
     *
     * @return The userDetails
     */
    public UserDetails getUserDetails() {
        if (userDetails == null) {
            userDetails = new UserDetails();
        }
        return userDetails;
    }

    /**
     * Check whether this User has UserDetails set.
     *
     * @return {@literal true} if set, otherwise {@literal false}
     */
    public boolean hasUserDetails() {
        return userDetails != null;
    }

    /**
     * Assign some details to the User.
     *
     * @param userDetails The userDetails to set
     */
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Does not call the superclass. Uses the username for calculation.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses the username for comparison.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the business key.
     *
     * @return Username
     */
    @Override
    public String toString() {
        return getUsername();
    }

    /**
     * A PasswordComparator sorts UserPassword by date ascending.
     *
     * @author Heiko Scherrer
     * @version $Revision$
     * @since 0.2
     */
    static class PasswordComparator implements Comparator<UserPassword>, Serializable {

        /**
         * {@inheritDoc}
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(UserPassword o1, UserPassword o2) {
            return o2.getPasswordChanged().compareTo(o1.getPasswordChanged());
        }
    }
}