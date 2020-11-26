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
import org.ameba.mapping.BeanMapper;
import org.openwms.core.uaa.admin.RoleService;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Optional;

/**
 * A RoleServiceImpl is a Spring managed transactional service that deals with {@link Role}s.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class RoleServiceImpl implements RoleService {

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
    public Collection<Role> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Role save(Role role) {
        Optional<Role> roleOpt = repository.findByName(role.getName());
        return roleOpt.map(value -> repository.save(mapper.mapFromTo(role, value))).
                orElseGet(() -> repository.save(role));
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