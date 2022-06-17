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
package org.openwms.core.uaa.admin;

import org.ameba.http.MeasuredRestController;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.openwms.core.uaa.admin.impl.SecurityObject;
import org.openwms.core.uaa.api.SecurityObjectVO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;

import static org.ameba.Constants.HEADER_VALUE_X_TENANT;
import static org.openwms.core.uaa.api.UAAConstants.API_GRANTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A GrantController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class GrantController extends AbstractWebController {

    private final SecurityService service;
    private final BeanMapper mapper;

    public GrantController(SecurityService service, BeanMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(API_GRANTS + "/index")
    public ResponseEntity<Index> index() {
        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(GrantController.class).findAllGrants()).withRel("grants-findall")
                )
        );
    }

    @Transactional(readOnly = true)
    @GetMapping(API_GRANTS)
    public ResponseEntity<List<SecurityObjectVO>> findAllGrants() {
        return ResponseEntity.ok(mapper.map(new ArrayList<>(service.findAllGrants()), SecurityObjectVO.class));
    }

    // TODO [openwms]: 17.06.22 Change header to IDENTITY
    @GetMapping(path = API_GRANTS, headers = HEADER_VALUE_X_TENANT)
    public ResponseEntity<List<SecurityObjectVO>> findAllForUser(@RequestHeader(HEADER_VALUE_X_TENANT) String user) {
        List<SecurityObject> grants = service.findAllFor(user);
        return ResponseEntity.ok(mapper.map(new ArrayList<>(grants), SecurityObjectVO.class));
    }
}
