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
package org.openwms.core.uaa.admin;

import org.openwms.core.uaa.admin.impl.Grant;
import org.openwms.core.uaa.admin.impl.SecurityObject;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A SecurityService defines functionality to handle {@code SecurityObject}s, especially {@code Grant}s.
 *
 * @author Heiko Scherrer
 * @version 0.2
 * @since 0.1
 */
public interface SecurityService {

    /**
     * Merge a list of persisted, detached or transient {@link Grant}s of a particular {@code Module}.
     *
     * @param moduleName The moduleName
     * @param grants The list of {@link Grant}s to merge
     * @return All existing {@link Grant}s
     */
    List<Grant> mergeGrants(@NotNull String moduleName, List<Grant> grants);

    /**
     * Find and return all existing {@link Grant}s.
     *
     * @return All existing {@link Grant}s
     */
    List<Grant> findAllGrants();

    /**
     * Find and return all {@link Grant}s assigned to an {@code User}.
     *
     * @param user The User's name
     * @return All Grants assigned to the User
     */
    List<SecurityObject> findAllFor(String user);
}