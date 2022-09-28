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
import org.openwms.core.uaa.admin.GrantService;
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.openwms.core.uaa.MessageCodes.MODULENAME_NOT_NULL;
import static org.openwms.core.uaa.MessageCodes.SO_WITH_PKEY_NOT_EXIST;

/**
 * A GrantServiceImpl is a transactional Spring Service implementation.
 *
 * @author Heiko Scherrer
 */
@TxService
class GrantServiceImpl implements GrantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrantServiceImpl.class);
    private final SecurityObjectRepository securityObjectRepository;
    private final UserRepository userRepository;
    private final Translator translator;

    GrantServiceImpl(SecurityObjectRepository securityObjectRepository, UserRepository userRepository, Translator translator) {
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
    public @NotNull List<Grant> mergeGrants(@NotBlank String moduleName, @NotNull List<Grant> grants) {
        Assert.notNull(moduleName, translator.translate(MODULENAME_NOT_NULL));
        LOGGER.debug("Merging grants of module: [{}]", moduleName);
        var persisted = securityObjectRepository.findAllOfModule(moduleName + "%");
        var result = new ArrayList<>(persisted);
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
    public @NotNull Grant findByPKey(@NotBlank String pKey) {
        return securityObjectRepository.findBypKey(pKey)
                .orElseThrow(() -> new NotFoundException(translator, SO_WITH_PKEY_NOT_EXIST, pKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull List<Grant> findAllGrants() {
        return securityObjectRepository.findAllGrants();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<@NotNull Grant> findAllFor(@NotBlank String user) {
        var userInstance = userRepository.findByUsername(user).orElseThrow(() -> new NotFoundException(format("User with name [%s] does not exist", user)));
        return userInstance.getGrants().stream().filter(g -> g instanceof Grant).map(g -> (Grant) g).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Create.class)
    public @NotNull Grant create(@NotNull(groups = ValidationGroups.Create.class) @Valid Grant grant) {
        return securityObjectRepository.save(grant);
    }
}