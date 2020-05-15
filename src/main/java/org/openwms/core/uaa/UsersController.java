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

    @GetMapping(UAAConstants.API_USERS + "/{pKey}")
    public ResponseEntity<UserVO> findUser(@PathVariable("pKey") String pKey) {
        return ResponseEntity.ok(mapper.map(service.findByPKey(pKey), UserVO.class));
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
