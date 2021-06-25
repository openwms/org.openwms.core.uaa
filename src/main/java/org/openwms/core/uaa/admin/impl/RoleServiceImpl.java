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
import org.ameba.mapping.BeanMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.lang.String.format;
import static org.openwms.core.uaa.MessageCodes.ROLE_WITH_PKEY_NOT_EXIST;

/**
 * A RoleServiceImpl is a Spring managed transactional service that deals with {@link Role}s.
 *
 * @author Heiko Scherrer
 */
@TxService
@Validated
class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository repository;
    private final UserService userService;
    private final BeanMapper mapper;
    private final Translator translator;

    RoleServiceImpl(RoleRepository repository, UserService userService, BeanMapper mapper, Translator translator) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
        this.translator = translator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<RoleVO> findAll() {
        return mapper.map(repository.findAll(), RoleVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public RoleVO findByPKey(String pKey) {
        return mapper.map(getRole(pKey), RoleVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Create.class)
    public RoleVO create(@NotNull(groups = ValidationGroups.Create.class) @Valid RoleVO role) {
        Role newRole = new Role();
        mapper.mapFromTo(role, newRole);
        newRole.setName(role.getName());
        if (repository.findByName(newRole.getName()).isPresent()) {
            throw new ResourceExistsException(format("Role with name [%s] already exists", role.getName()));
        }
        newRole = repository.save(newRole);
        LOGGER.debug("Created Role [{}]", newRole);
        return mapper.map(newRole, RoleVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    @Validated(ValidationGroups.Modify.class)
    public RoleVO save(@NotEmpty String pKey, @NotNull(groups = ValidationGroups.Modify.class) @Valid RoleVO role) {
        Role existingRole = getRole(pKey);
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        return mapper.map(repository.save(existingRole), RoleVO.class);
    }

    private Role getRole(String pKey) {
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
    public void delete(@NotEmpty String pKey) {
        repository.deleteByPKey(pKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public RoleVO assignUser(String pKey, String userPKey) {
        Role role = getRole(pKey);
        User user = userService.findByPKey(userPKey);
        role.addUser(user);
        role = repository.save(role);
        return mapper.map(role, RoleVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public RoleVO unassignUser(String pKey, String userPKey) {
        Role role = getRole(pKey);
        User user = userService.findByPKey(userPKey);
        role.removeUser(user);
        role = repository.save(role);
        return mapper.map(role, RoleVO.class);
    }
}