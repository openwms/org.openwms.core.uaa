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
package org.openwms.core.uaa.auth.impl;

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.ameba.exception.NotFoundException;
import org.openwms.core.uaa.auth.Client;
import org.openwms.core.uaa.auth.ClientService;
import org.openwms.core.uaa.client.ClientMapper;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.lang.String.format;

/**
 * A ClientServiceImpl is a Spring managed transactional Service.
 *
 * @author Heiko Scherrer
 */
@Validated
@TxService
class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper mapper;

    ClientServiceImpl(ClientRepository clientRepository, ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Client create(@NotNull Client client) {
        return clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public Client save(@NotNull Client client) {
        Client saved = clientRepository.findBypKey(client.getPersistentKey()).orElseThrow(() -> new NotFoundException(format("No Client with PKey [%s] exists", client.getPersistentKey())));
        mapper.copy(client, saved);
        return clientRepository.save(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public void delete(@NotEmpty String pKey) {
        clientRepository.deleteByPKey(pKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
