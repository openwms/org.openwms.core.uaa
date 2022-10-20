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
import org.openwms.core.uaa.admin.RoleMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.openwms.core.uaa.MessageCodes.ROLE_WITH_PKEY_NOT_EXIST;

/**
 * A RoleServiceImpl is a Spring managed transactional service that deals with {@link Role}s.
 *
 * @author Heiko Scherrer
 */
@TxService
class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository repository;
    private final UserService userService;
    private final RoleMapper mapper;
    private final Translator translator;
    private final ApplicationEventPublisher eventPublisher;

    RoleServiceImpl(RoleRepository repository, UserService userService, RoleMapper mapper, Translator translator, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
        this.translator = translator;
        this.eventPublisher = eventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull List<RoleVO> findAll() {
        return mapper.convertToVO(repository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull RoleVO findByPKey(@NotBlank String pKey) {
        return mapper.convertToVO(findByPKeyInternal(pKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull List<Role> findByNames(@NotNull List<String> roleNames) {
        var allRoles = repository.findByNameIn(roleNames);
        return allRoles == null ? new ArrayList<>(0) : allRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Create.class)
    public @NotNull RoleVO create(@NotNull(groups = ValidationGroups.Create.class) @Valid RoleVO role) {
        var newRole = mapper.convertFrom(role);
        newRole.setName(role.getName());
        if (repository.findByName(newRole.getName()).isPresent()) {
            throw new ResourceExistsException(format("Role with name [%s] already exists", role.getName()));
        }
        newRole = repository.save(newRole);
        eventPublisher.publishEvent(new RoleEvent(newRole, RoleEvent.EventType.CREATED));
        LOGGER.debug("Created Role [{}]", newRole);
        return mapper.convertToVO(newRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Modify.class)
    public @NotNull RoleVO save(@NotBlank String pKey, @NotNull(groups = ValidationGroups.Modify.class) @Valid RoleVO role) {
        var existingRole = findByPKeyInternal(pKey);
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole = repository.save(existingRole);
        eventPublisher.publishEvent(new RoleEvent(existingRole, RoleEvent.EventType.MODIFIED));
        return mapper.convertToVO(existingRole);
    }

    private Role findByPKeyInternal(String pKey) {
        return repository.findBypKey(pKey).orElseThrow(() -> new NotFoundException(
                translator.translate(ROLE_WITH_PKEY_NOT_EXIST, pKey),
                ROLE_WITH_PKEY_NOT_EXIST,
                pKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotBlank String pKey) {
        var role = repository.findBypKey(pKey);
        if (role.isPresent()) {
            repository.deleteByPKey(pKey);
            eventPublisher.publishEvent(new RoleEvent(role.get(), RoleEvent.EventType.DELETED));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull RoleVO assignUser(@NotBlank String pKey, @NotBlank String userPKey) {
        var role = findByPKeyInternal(pKey);
        var user = userService.findByPKey(userPKey);
        role.addUser(user);
        role = repository.save(role);
        eventPublisher.publishEvent(new RoleEvent(role, RoleEvent.EventType.MODIFIED));
        return mapper.convertToVO(role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public @NotNull RoleVO unassignUser(@NotBlank String pKey, @NotBlank String userPKey) {
        var role = findByPKeyInternal(pKey);
        var user = userService.findByPKey(userPKey);
        role.removeUser(user);
        role = repository.save(role);
        eventPublisher.publishEvent(new RoleEvent(role, RoleEvent.EventType.MODIFIED));
        return mapper.convertToVO(role);
    }
}