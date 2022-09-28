/*
 * Copyright 2005-2021 the original author or authors.
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

import org.openwms.core.uaa.events.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * A UAAEventListener.
 *
 * @author Heiko Scherrer
 */
@Component
class UAAEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UAAEventListener.class);

    @TransactionalEventListener
    public void onPickOrderPositionSplitEvent(UserEvent event) {
        var user = event.getSource();
        user.wipePassword();
        LOGGER.info("UAA UserEvent: [{}] : [{}]", event.getType(), user);
    }
}
