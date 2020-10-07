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
import org.openwms.core.uaa.api.SecurityObjectVO;
import org.openwms.core.uaa.api.UAAConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * A GrantsController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class GrantsController extends AbstractWebController {

    private final SecurityService service;
    private final BeanMapper mapper;

    public GrantsController(SecurityService service, BeanMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    @GetMapping(UAAConstants.API_GRANTS)
    public ResponseEntity<List<SecurityObjectVO>> findAllGrants() {
        return ResponseEntity.ok(mapper.map(new ArrayList<>(service.findAllGrants()), SecurityObjectVO.class));
    }
}
