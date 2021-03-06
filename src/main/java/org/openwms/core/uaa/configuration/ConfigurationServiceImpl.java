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
package org.openwms.core.uaa.configuration;

import org.springframework.stereotype.Service;

/**
 * A ConfigurationServiceImpl.
 *
 * @author Heiko Scherrer
 */
@Service
class ConfigurationServiceImpl implements ConfigurationService {

    /**
     * Save preferences of an {@code User}.
     *
     * @param preference The encapsulated preferences instance to save
     * @return The stored instance
     */
    @Override
    public UserPreference save(UserPreference preference) {
        return null;
    }
}
