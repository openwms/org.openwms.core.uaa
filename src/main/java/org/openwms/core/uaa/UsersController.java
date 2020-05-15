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

import org.ameba.http.MeasuredRestController;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.impl.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
 * An UsersController represents a RESTful access to {@code User}s. It is transactional by the means it is the outer application service
 * facade that returns validated and completed {@code User} objects to its clients.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class UsersController extends AbstractWebController {

    private final UserService service;
    private final BeanMapper mapper;

    public UsersController(UserService service, BeanMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(UAAConstants.API_USERS)
    public ResponseEntity<List<UserVO>> findAllUsers() {
        return ResponseEntity.ok(mapper.map(new ArrayList<>(service.findAll()), UserVO.class));
    }

    @PostMapping(UAAConstants.API_USERS)
    public ResponseEntity<Void> create(@RequestBody @Valid @NotNull UserVO user, HttpServletRequest req) {
        return ResponseEntity.created(
                getLocationURIForCreatedResource(req, service.create(mapper.map(user, User.class)).getPersistentKey())
        ).build();
    }

    @PutMapping(UAAConstants.API_USERS)
    public ResponseEntity<UserVO> save(@RequestBody @Valid UserVO user) {
        return ResponseEntity.ok(mapper.map(service.save(mapper.map(user, User.class)), UserVO.class));
    }

    @PatchMapping(UAAConstants.API_USERS + "/{pKey}")
    public void saveImage(@RequestBody @NotNull byte[] image, @PathVariable("pKey") @NotNull String pKey) {
        service.uploadImageFile(pKey, image);
    }
}
