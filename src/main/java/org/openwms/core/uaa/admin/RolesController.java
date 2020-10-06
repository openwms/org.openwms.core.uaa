/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
