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
import org.ameba.exception.NotFoundException;
import org.ameba.exception.ResourceExistsException;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

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
    private final BeanMapper mapper;

    RoleServiceImpl(RoleRepository repository, BeanMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        Optional<Role> roleOpt = repository.findBypKey(pKey);
        if (roleOpt.isEmpty()) {
            throw new NotFoundException(format("Role with pKey [%s] does not exist", pKey));
        }
        Role existingRole = roleOpt.get();
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        return mapper.map(repository.save(existingRole), RoleVO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotEmpty String pKey) {
        repository.deleteByPKey(pKey);
    }
}