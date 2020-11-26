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
package org.openwms.core.search;

import org.openwms.core.uaa.admin.impl.User;

import java.util.Collection;

/**
 * An ActionService is used to query Actions of a particular User or the whole application.
 *
 * @author Heiko Scherrer
 * @see Action
 */
public interface ActionService {

    /**
     * Find and return all defined {@link Action}s.
     *
     * @return All {@link Action}s
     */
    Collection<Action> findAllActions();

    /**
     * Find and return all {@link Action}s of an {@link User}.
     *
     * @param user The {@link User} to search for
     * @return All {@link Action}s
     */
    Collection<Action> findAllActions(User user);

    /**
     * Find and return all {@link Tag}s the {@link User} user has used so far.
     *
     * @param user The {@link User} to find the {@link Tag}s for
     * @return All used {@link Tag}s
     */
    Collection<Tag> findAllTags(User user);

    /**
     * Save a Collection of {@link Action} for a specific {@link User}.
     *
     * @param user The {@link User} of the {@link Action}
     * @param actions The Collection of {@link Action}s to save
     * @return The saved Collection
     */
    Collection<Action> save(User user, Collection<Action> actions);
}