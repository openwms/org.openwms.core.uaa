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
package org.openwms.core.uaa.auth;

import org.ameba.http.MeasuredRestController;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.openwms.core.uaa.api.ClientVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.openwms.core.uaa.api.UAAConstants.API_CLIENTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A ClientController.
 *
 * @author Heiko Scherrer
 */
@Validated
@MeasuredRestController
public class ClientController extends AbstractWebController {

    private final ClientService clientService;
    private final ClientMapper mapper;

    public ClientController(ClientService clientService, ClientMapper mapper) {
        this.clientService = clientService;
        this.mapper = mapper;
    }

    @GetMapping(API_CLIENTS + "/index")
    public ResponseEntity<Index> index() {

        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(ClientController.class).findAll()).withRel("clients-findall"),
                        linkTo(methodOn(ClientController.class).findByPKey("{pKey}")).withRel("clients-findbypkey"),
                        linkTo(methodOn(ClientController.class).create(new ClientVO(), null)).withRel("clients-create"),
                        linkTo(methodOn(ClientController.class).save(new ClientVO())).withRel("clients-save"),
                        linkTo(methodOn(ClientController.class).delete("{pKey}")).withRel("clients-delete")
                )
        );
    }

    @GetMapping(API_CLIENTS + "/{pKey}")
    public ResponseEntity<ClientVO> findByPKey(@PathVariable("pKey") String pKey) {

        var eo = clientService.findByPKey(pKey);
        var result = mapper.to(eo);
        addSelfLink(result);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, ClientVO.MEDIA_TYPE)
                .body(result);
    }

    @GetMapping(API_CLIENTS)
    public ResponseEntity<List<ClientVO>> findAll() {

        var eos = clientService.findAll();
        var result = mapper.clientsToClientVos(eos);
        result.forEach(this::addSelfLink);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, ClientVO.MEDIA_TYPE)
                .body(result);
    }

    @PostMapping(API_CLIENTS)
    @Validated(ValidationGroups.Create.class)
    public ResponseEntity<ClientVO> create(@RequestBody @Valid @NotNull ClientVO client, HttpServletRequest req) {

        var eo = clientService.create(mapper.from(client));
        var result = mapper.to(eo);
        return ResponseEntity
                .created(getLocationURIForCreatedResource(req, result.getpKey()))
                .header(HttpHeaders.CONTENT_TYPE, ClientVO.MEDIA_TYPE)
                .body(result);
    }

    @PutMapping(API_CLIENTS)
    public ResponseEntity<ClientVO> save(@RequestBody @Valid ClientVO client) {

        var eo = clientService.save(mapper.from(client));
        var result = mapper.to(eo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, ClientVO.MEDIA_TYPE)
                .body(result);
    }

    @DeleteMapping(API_CLIENTS + "/{pKey}")
    public ResponseEntity<Void> delete(@PathVariable("pKey") String pKey) {

        clientService.delete(pKey);
        return ResponseEntity.noContent().build();
    }

    private void addSelfLink(ClientVO result) {
        result.add(linkTo(methodOn(ClientController.class).findByPKey(result.getpKey())).withRel("client-findbypkey"));
    }
}
