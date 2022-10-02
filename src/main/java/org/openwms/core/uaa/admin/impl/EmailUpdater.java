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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;

/**
 * A EmailUpdater.
 *
 * @author Heiko Scherrer
 */
@Transactional(propagation = Propagation.MANDATORY)
@Service
class EmailUpdater implements UserUpdater {

    @Override
    public User update(User existingInstance, User newInstance) {
        if (newInstance == null) {
            return existingInstance;
        }
        var newEmailAddresses = newInstance.getEmailAddresses();

        if (existingInstance.getEmailAddresses() != null) {
            if (newEmailAddresses != null) {

                // Remove existing orphans
                for (var email : existingInstance.getEmailAddresses()) {
                    if (!newEmailAddresses.contains(email)) {
                        existingInstance.removeEmailAddress(email);
                    } else {
                        var current = newEmailAddresses.stream().filter(e -> e.equals(email)).findFirst().orElseThrow();
                        if (email.isPrimary() != current.isPrimary()) {
                            email.setPrimary(current.isPrimary());
                        }
                    }
                }

                // Add new ones
                newEmailAddresses.forEach(existingInstance::addNewEmailAddress);
            } else {
                existingInstance.setEmailAddresses(null);
            }
        } else {
            if (newEmailAddresses != null) {
                existingInstance.setEmailAddresses(new LinkedHashSet<>(newEmailAddresses));
            }
        }
        return existingInstance;
    }

    @Override
    public boolean supports(String delimiter) {
        return true;
    }
}
