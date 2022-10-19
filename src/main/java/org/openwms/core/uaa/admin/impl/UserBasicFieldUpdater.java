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

import org.ameba.annotation.Measured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A UserBasicFieldUpdater.
 *
 * @author Heiko Scherrer
 */
@Transactional(propagation = Propagation.MANDATORY)
@Service
class UserBasicFieldUpdater implements UserUpdater {

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public User update(User existingInstance, User newInstance) {
        if ( newInstance == null ) {
            return existingInstance;
        }

        if ( newInstance.hasPersistentKey() ) {
            existingInstance.setPersistentKey( newInstance.getPersistentKey() );
        }
        existingInstance.setOl( newInstance.getOl() );
        existingInstance.setUsername( newInstance.getUsername() );
        existingInstance.setExternalUser( newInstance.isExternalUser() );
        existingInstance.setLastPasswordChange( newInstance.getLastPasswordChange() );
        existingInstance.setLocked( newInstance.isLocked() );
        existingInstance.setEnabled( newInstance.isEnabled() );
        existingInstance.setExpirationDate( newInstance.getExpirationDate() );
        existingInstance.setFullname( newInstance.getFullname() );
        if ( newInstance.hasUserDetails() ) {
            existingInstance.setUserDetails( newInstance.getUserDetails() );
        }
        else {
            existingInstance.setUserDetails( null );
        }

        return existingInstance;
    }

    @Override
    public boolean supports(String delimiter) {
        return true ;
    }
}
