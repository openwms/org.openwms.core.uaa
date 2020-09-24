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
package org.openwms.core.uaa.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.openwms.core.uaa.api.ClientVO;
import org.openwms.core.uaa.auth.Client;

import java.util.List;

/**
 * A ClientMapper.
 *
 * @author Heiko Scherrer
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

    List<ClientVO> clientsToClientVos(List<Client> clients);

    @Mapping(source = "persistentKey", target = "pKey")
    ClientVO to(Client entity);

    @Mapping(source = "pKey", target = "persistentKey")
    Client from(ClientVO vo);

    void copy(Client client, @MappingTarget Client target);

}
