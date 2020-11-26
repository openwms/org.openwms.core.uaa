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
package org.openwms.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * A TimeProvider is able to provide dates and times.
 *
 * @author Heiko Scherrer
 */
public interface TimeProvider {

    /**
     * Returns the current date and time considering the configured timezone.
     *
     * @return Timezone aware Date
     */
    default Date nowAsDate() {
        return Date.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns the current date and time considering the configured timezone.
     *
     * @return Timezone aware DateTime
     */
    default ZonedDateTime nowAsZonedDateTime() {
        return nowAsZonedDateTime(ZoneId.systemDefault());
    }

    /**
     * Returns the current date and time considering the configured timezone.
     *
     * @param zoneId ZoneId
     * @return Timezone aware DateTime
     */
    default ZonedDateTime nowAsZonedDateTime(ZoneId zoneId) {
        return ZonedDateTime.of(LocalDateTime.now(), zoneId);
    }

    /**
     * Returns the current date and time of the system considering the configured timezone.
     *
     * @return Timezone aware Date
     */
    default Instant now() {
        return ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant();
    }
}
