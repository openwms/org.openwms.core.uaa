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
package org.openwms.core.uaa;

import org.ameba.http.Response;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.http.HttpBusinessException;
import org.openwms.core.uaa.impl.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A RolesController.
 *
 * @author Heiko Scherrer
 */
@RestController
public class RolesController {

    private final RoleService service;
    private final BeanMapper m;

    public RolesController(RoleService service, BeanMapper m) {
        this.service = service;
        this.m = m;
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/EYAWAQ
     *
     * @return JSON response
     * @status Reviewed [scherrer]
     */
    @GetMapping(UAAConstants.API_ROLES)
    public List<RoleVO> findAllRoles() {
        List<RoleVO> roles = m.map(new ArrayList<>(service.findAll()), RoleVO.class);
        return roles;
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/BIAWAQ
     *
     * @param role The {@link Role} instance to be created
     * @return An {@link Response} object to encapsulate the result of the creation operation
     * @status Reviewed [scherrer]
     */
    @PostMapping(UAAConstants.API_ROLES)
    public RoleVO create(@RequestBody @Valid @NotNull RoleVO role, HttpServletRequest req, HttpServletResponse resp) {
        RoleVO createdRole = m.map(service.save(m.map(role, Role.class)), RoleVO.class);
        // FIXME [openwms]: 02.12.19
        resp.addHeader(HttpHeaders.LOCATION, getLocationForCreatedResource(req,""));
        return createdRole;
    }

    private String getLocationForCreatedResource(HttpServletRequest req, String objId) {
        StringBuffer url = req.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{objId}/").toString());
        return template.expand(objId).toASCIIString();
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/BoAWAQ
     *
     * @param rolenames An array of role names to delete
     * @return An {@link Response} object to encapsulate all single removal operations
     * @status Reviewed [scherrer]
     */
    @DeleteMapping(UAAConstants.API_ROLES + "/{name}")
    public ResponseEntity<Response> remove(@PathVariable("name") @NotNull String... rolenames) {
        /*
        Response result = new Response();
        HttpStatus resultStatus = HttpStatus.OK;
        for (String rolename : rolenames) {
            if (rolename == null || rolename.isEmpty()) {
                continue;
            }
            try {
                service.removeByBK(new String[]{rolename});
                result.add(new Response.ItemBuilder().wStatus(HttpStatus.OK).wParams(rolename).build());
            } catch (Exception sre) {
                resultStatus = HttpStatus.NOT_FOUND;
                Response.ResponseItem item = new Response.ItemBuilder().wMessage(sre.getMessage())
                        .wStatus(HttpStatus.INTERNAL_SERVER_ERROR).wParams(rolename).build();
                if (NotFoundException.class.equals(sre.getClass())) {
                    item.httpStatus = HttpStatus.NOT_FOUND;
                }
                result.add(item);
            }
        }
        return new ResponseEntity<ResponseVO>(result, resultStatus);
        */
        return null;
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param role
     * @return
     */
    @PutMapping(UAAConstants.API_ROLES)
    @ResponseStatus(HttpStatus.OK)
    public RoleVO save(@RequestBody @Valid RoleVO role) {
        // FIXME [openwms]: 02.12.19 check if this is really the name
        if (role.getName() == null) {
            throw new HttpBusinessException("Role to save is a transient one", HttpStatus.NOT_ACCEPTABLE);
        }
        Role toSave = m.map(role, Role.class);
        return m.map(service.save(toSave), RoleVO.class);
    }
}
