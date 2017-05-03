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

import net.sf.ehcache.Ehcache;
import org.ameba.annotation.TxService;
import org.openwms.core.event.UserChangedEvent;
import org.openwms.core.uaa.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * A SecurityContextUserServiceImpl extends Spring {@link UserDetailsService} to
 * read {@code User}s and {@code Role}s from the persistent storage and wraps them into security objects.
 *
 * @author <a href="mailto:russelltina@users.sourceforge.net">Tina Russell</a>
 */
@TxService
class SecurityContextUserServiceImpl implements UserDetailsService, ApplicationListener<UserChangedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextUserServiceImpl.class);

    @Value("${system.user}")
    private String systemUsername = SystemUser.SYSTEM_USERNAME;
    private final UserService userService;
    private UserCache userCache;
    private Ehcache cache;
    private final PasswordEncoder enc;

    public SecurityContextUserServiceImpl(UserService userService, PasswordEncoder enc) {
        this.userService = userService;
        this.enc = enc;
    }

    @Autowired(required = false)
    void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    @Autowired(required = false)
    void setCache(Ehcache cache) {
        this.cache = cache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(UserChangedEvent event) {
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
                User user = userService.createSystemUser();
                ud = new SystemUserWrapper(user);
                ((SystemUserWrapper) ud).setPassword(enc.encode(user.getPassword()));
            } else {
                ud = new UserWrapper(
                        userService.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username [%s] not found", username))));
            }
            if (userCache != null) {
                userCache.putUserInCache(ud);
            }
        }
        return ud;
    }
}