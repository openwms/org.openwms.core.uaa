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
import org.ameba.exception.ResourceExistsException;
import org.ameba.i18n.Translator;
import org.openwms.core.uaa.admin.GrantService;
import org.openwms.core.uaa.api.ValidationGroups;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.openwms.core.uaa.MessageCodes.GRANT_WITH_NAME_ALREADY_EXISTS;
import static org.openwms.core.uaa.MessageCodes.SO_WITH_PKEY_NOT_EXIST;
import static org.openwms.core.uaa.MessageCodes.USER_WITH_NAME_NOT_EXIST;

/**
 * A GrantServiceImpl is a transactional Spring Service implementation.
 *
 * @author Heiko Scherrer
 */
@TxService
class GrantServiceImpl implements GrantService {

    private final GrantRepository grantRepository;
    private final UserRepository userRepository;
    private final Translator translator;
    private final ApplicationEventPublisher eventPublisher;

    GrantServiceImpl(GrantRepository grantRepository, UserRepository userRepository, Translator translator,
            ApplicationEventPublisher eventPublisher) {
        this.grantRepository = grantRepository;
        this.userRepository = userRepository;
        this.translator = translator;
        this.eventPublisher = eventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull Grant findByPKey(@NotBlank String pKey) {
        return grantRepository.findBypKey(pKey)
                .orElseThrow(() -> new NotFoundException(translator, SO_WITH_PKEY_NOT_EXIST, pKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull List<Grant> findAllGrants() {
        return grantRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<@NotNull Grant> findAllFor(@NotBlank String username) {
        var userInstance = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(translator, USER_WITH_NAME_NOT_EXIST, username));
        return userInstance.getGrants().stream().filter(Grant.class::isInstance).map(Grant.class::cast).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Create.class)
    public @NotNull Grant create(@NotNull(groups = ValidationGroups.Create.class) @Valid Grant grant) {
        if (grantRepository.findByName(grant.getName()).isPresent()) {
            throw new ResourceExistsException(translator, GRANT_WITH_NAME_ALREADY_EXISTS, grant.getName());
        }
        grant = grantRepository.save(grant);
        eventPublisher.publishEvent(new GrantEvent(grant, GrantEvent.EventType.CREATED));
        return grant;
    }
}