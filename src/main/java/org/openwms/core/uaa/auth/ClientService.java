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
package org.openwms.core.uaa.auth;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A ClientService is a CRUD service dealing with {@link Client}s.
 *
 * @author Heiko Scherrer
 */
public interface ClientService {

    /**
     * Save a non-existing {@link Client} instance.
     *
     * @param client The instance to save
     * @return The saved instance
     */
    Client create(@NotNull Client client);

    /**
     * Update an existing {@link Client} instance.
     *
     * @param client The instance to save
     * @return The saved instance
     */
    Client save(@NotNull Client client);

    /**
     * Delete a {@link Client}.
     *
     * @param pKey The identifiable persistent key
     */
    void delete(@NotEmpty String pKey);

    /**
     * Find and return all existing {@link Client}s.
     *
     * @return A list of those
     */
    List<Client> findAll();
}
