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

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.ameba.i18n.Translator;
import org.openwms.core.annotation.FireAfterTransaction;
import org.openwms.core.event.UserChangedEvent;
import org.openwms.core.uaa.admin.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.openwms.core.uaa.MessageCodes.MODULENAME_NOT_NULL;

/**
 * A SecurityServiceImpl is a transactional Spring Service implementation.
 *
 * @author Heiko Scherrer
 */
@TxService
class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);
    private final SecurityObjectRepository securityObjectRepository;
    private final UserRepository userRepository;
    private final Translator translator;

    SecurityServiceImpl(SecurityObjectRepository securityObjectRepository, UserRepository userRepository, Translator translator) {
        this.securityObjectRepository = securityObjectRepository;
        this.userRepository = userRepository;
        this.translator = translator;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fires {@code UserChangedEvent} after completion.
     */
    @Override
    @FireAfterTransaction(events = {UserChangedEvent.class})
    @Measured
    public List<Grant> mergeGrants(String moduleName, List<Grant> grants) {
        Assert.notNull(moduleName, translator.translate(MODULENAME_NOT_NULL));
        LOGGER.debug("Merging grants of module: [{}]", moduleName);
        List<Grant> persisted = securityObjectRepository.findAllOfModule(moduleName + "%");
        List<Grant> result = new ArrayList<>(persisted);
        grants.forEach(g -> {
            if (persisted.contains(g)) persisted.remove(g);
            else result.add(securityObjectRepository.save(g));
        });
        result.removeAll(persisted);
        if (!persisted.isEmpty()) {
            securityObjectRepository.deleteAll(persisted);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<Grant> findAllGrants() {
        return securityObjectRepository.findAllGrants();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<SecurityObject> findAllFor(String user) {
        User userInstance = userRepository.findByUsername(user).orElseThrow(() -> new NotFoundException(format("User with name [%s] does not exist", user)));
        return userInstance.getGrants().stream().filter(g -> g instanceof Grant).collect(Collectors.toList());
    }
}