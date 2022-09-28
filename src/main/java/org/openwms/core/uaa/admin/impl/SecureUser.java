/*
 * Copyright 2005-2022 the original author or authors.
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

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A SecureUser.
 *
 * @author Heiko Scherrer
 */
@JacksonAware
public final class SecureUser implements UserDetails, Serializable {

    private String username;
    private char[] password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<? extends GrantedAuthority> authorities;

    @JsonCreator
    SecureUser() {}

    public SecureUser(final String username, final String password, final boolean accountNonExpired, final boolean accountNonLocked,
            final boolean credentialsNonExpired, final boolean enabled, final Collection<SecurityObjectAuthority> securityObjectAuthorities) {
        Assert.hasText(username, "username must not be null");
        Assert.hasText(password, "password must not be null");
        this.username = username;
        this.password = password.toCharArray();
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        if (securityObjectAuthorities != null) {
            authorities = securityObjectAuthorities.stream()
                    .map(soa -> new SimpleGrantedAuthority(soa.getAuthority()))
                    .collect(Collectors.toList());
        } else {
            authorities = new ArrayList<>();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return String.valueOf(password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
