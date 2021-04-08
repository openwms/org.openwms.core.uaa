package org.openwms.core.uaa.admin.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * An UserWrapper is used as an adapter between {@link Role}s, {@link SecurityObject}s and Spring's {@link GrantedAuthority} objects.
 *
 * @author <a href="mailto:russelltina@users.sourceforge.net">Tina Russell</a>
 * @see SecurityObject
 * @see Role
 * @see org.springframework.security.core.GrantedAuthority
 * @see org.springframework.security.core.userdetails.UserDetails
 */
public class UserWrapper implements UserDetails, UserHolder, Serializable {

    private static final long serialVersionUID = 1L;
    private final User user;
    private Collection<GrantedAuthority> authorities = null;

    /**
     * Create a new UserWrapper.
     *
     * @param user The User to wrap
     */
    @SuppressWarnings("squid:S2201") // call to .size() in order to trigger lazy loading
    public UserWrapper(User user) {
        Assert.notNull(user, "Not allowed to create an UserWrapper with null argument");
        this.user = user;
        this.user.getRoles().size();
    }

    /**
     * Subclasses can set a collection of grants that are always available for an User. This is useful for administrative accounts.
     *
     * @param authz A collection of grants (authorities) where the default grants are added to
     */
    protected void addDefaultGrants(Collection<GrantedAuthority> authz) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @return the authorities, sorted by natural key (never {@literal null})
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (null == authorities) {
            authorities = new HashSet<>();
            authorities.addAll(user.getGrants().stream().map(SecurityObjectAuthority::new).collect(Collectors.toList()));
            addDefaultGrants(authorities);
        }
        return authorities;
    }

    /**
     * {@inheritDoc}
     *
     * @return the password (never {@literal null})
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * {@inheritDoc}
     *
     * @return the username (never {@literal null})
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@literal true} if the user's account is valid (ie non-expired), {@literal false} if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@literal true} if the user is not locked, {@literal false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@literal true} if the user's credentials are valid (ie non-expired), {@literal false} if no longer valid (ie expired)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@literal true} if the user is enabled, {@literal false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses authorities and user for calculation.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Uses authorities and user for comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserWrapper other = (UserWrapper) obj;
        if (authorities == null) {
            if (other.authorities != null) {
                return false;
            }
        } else if (!authorities.equals(other.authorities)) {
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

    /**
     * Return the Users username.
     *
     * @return The username of the User
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getUsername();
    }
}