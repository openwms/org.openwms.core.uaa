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

import org.ameba.http.MeasuredRestController;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.uaa.api.ClientVO;
import org.openwms.core.uaa.auth.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * A ClientController.
 *
 * @author Heiko Scherrer
 */
@CrossOrigin("*")
@Validated
@MeasuredRestController
public class ClientController extends AbstractWebController {

    private final ClientService clientService;
    private final ClientMapper mapper;

    public ClientController(ClientService clientService, ClientMapper mapper) {
        this.clientService = clientService;
        this.mapper = mapper;
    }

    @GetMapping("/api/clients")
    public ResponseEntity<List<ClientVO>> findAll() {
        return ResponseEntity.ok(mapper.clientsToClientVos(clientService.findAll()));
    }

    @PostMapping("/api/clients")
    public ResponseEntity<ClientVO> create(@RequestBody @Valid ClientVO client) {
        return ResponseEntity.ok(mapper.to(clientService.create(mapper.from(client))));
    }

    @PutMapping("/api/clients")
    public ResponseEntity<ClientVO> save(@RequestBody @Valid ClientVO client) {
        return ResponseEntity.ok(mapper.to(clientService.save(mapper.from(client))));
    }

    @DeleteMapping("/api/clients/{pKey}")
    public ResponseEntity<Void> delete(@PathVariable("pKey") String pKey) {
        clientService.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
