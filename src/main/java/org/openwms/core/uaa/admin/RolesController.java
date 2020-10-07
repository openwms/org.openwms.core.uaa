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
import org.openwms.core.http.HttpBusinessException;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.api.UAAConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A RolesController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class RolesController extends AbstractWebController {

    private final RoleService service;
    private final BeanMapper mapper;

    public RolesController(RoleService service, BeanMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/EYAWAQ
     **/
    @Transactional(readOnly = true)
    @GetMapping(UAAConstants.API_ROLES)
    public ResponseEntity<List<RoleVO>> findAllRoles() {
        return ResponseEntity.ok(mapper.map(new ArrayList<>(service.findAll()), RoleVO.class));
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/BIAWAQ
     */
    @PostMapping(UAAConstants.API_ROLES)
    public ResponseEntity<RoleVO> create(@RequestBody @Valid @NotNull RoleVO role, HttpServletRequest req) {
        return ResponseEntity.ok(
                mapper.map(service.save(mapper.map(role, Role.class)), RoleVO.class)
        );
    }

    @PutMapping(UAAConstants.API_ROLES)
    public ResponseEntity<RoleVO> save(@RequestBody @Valid @NotNull RoleVO role) {
        if (role.getName() == null) {
            throw new HttpBusinessException("Role to save is a transient one", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(mapper.map(service.save(mapper.map(role, Role.class)), RoleVO.class));
    }

    @DeleteMapping(UAAConstants.API_ROLES + "/{pKey}")
    public ResponseEntity<Void> delete(@PathVariable("pKey") String pKey) {
        service.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
