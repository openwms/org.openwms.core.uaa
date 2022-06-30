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

import net.sf.ehcache.Ehcache;
import org.ameba.annotation.TxService;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.events.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;

/**
 * A SecurityContextUserServiceImpl extends Spring {@link UserDetailsService} to
 * read {@code User}s and {@code Role}s from the persistent storage and wraps them into security objects.
 *
 * @author <a href="mailto:russelltina@users.sourceforge.net">Tina Russell</a>
 */
@TxService
class SecurityContextUserServiceImpl implements UserDetailsService, ApplicationListener<UserEvent> {

    private final String systemUsername;
    private final UserService userService;
    private final UserCache userCache;
    private final Ehcache cache;
    private final PasswordEncoder enc;

    public SecurityContextUserServiceImpl(
            @Value("${owms.security.system.username:}") String systemUsername,
            UserService userService,
            @Autowired(required = false) UserCache userCache,
            @Autowired(required = false) Ehcache cache,
            PasswordEncoder enc
    ) {
        this.systemUsername = systemUsername == null ? SystemUser.SYSTEM_USERNAME : systemUsername;
        this.userService = userService;
        this.userCache = userCache;
        this.cache = cache;
        this.enc = enc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(UserEvent event) {
        if (cache != null) {
            cache.removeAll();
        }
    }

    /**
     * {@inheritDoc}
     *
     * Implemented as read-only transactional
     **/
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails ud = userCache == null ? null : userCache.getUserFromCache(username);
        if (null == ud) {
            if (systemUsername.equals(username)) {
                var user = userService.createSystemUser();
                ud = new SecureUser(
                        systemUsername,
                        enc.encode(user.getPassword()),
                        true,
                        true,
                        true,
                        true,
                        asList(new SecurityObjectAuthority(SystemUser.SYSTEM_ROLE_NAME))
                );
            } else {
                var user = userService
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username [%s] not found", username)));
                ud = new SecureUser(
                        username,
                        user.getPassword(),
                        user.getExpirationDate() == null || user.getExpirationDate().isAfter(now()),
                        !user.isLocked(),
                        true,
                        user.isEnabled(),
                        user.getGrants().stream().map(SecurityObjectAuthority::new).collect(Collectors.toList())
                        );
            }
            if (userCache != null) {
                userCache.putUserInCache(ud);
            }
        }
        return ud;
    }
}