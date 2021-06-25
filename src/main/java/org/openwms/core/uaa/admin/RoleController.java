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
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.SecurityObjectVO;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
import java.util.ArrayList;
import java.util.List;

import static org.openwms.core.uaa.api.UAAConstants.API_ROLES;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A RoleController.
 *
 * @author Heiko Scherrer
 */
@Validated
@MeasuredRestController
public class RoleController extends AbstractWebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping(API_ROLES + "/index")
    public ResponseEntity<Index> index() {
        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(RoleController.class).findAllRoles()).withRel("roles-findall"),
                        linkTo(methodOn(RoleController.class).findRoleByPKey("pKey")).withRel("roles-findbypkey"),
                        linkTo(methodOn(RoleController.class).findUsersOfRole("pKey")).withRel("roles-findusersofrole"),
                        linkTo(methodOn(RoleController.class).findGrantsOfRole("pKey")).withRel("roles-findgrantsofrole"),
                        linkTo(methodOn(RoleController.class).create(new RoleVO(), null)).withRel("roles-create"),
                        linkTo(methodOn(RoleController.class).save("pKey", new RoleVO())).withRel("roles-save"),
                        linkTo(methodOn(RoleController.class).delete("pKey")).withRel("roles-delete"),
                        linkTo(methodOn(RoleController.class).assignUserToRole("pKey", "userPKey")).withRel("roles-assignuser"),
                        linkTo(methodOn(RoleController.class).unassignUser("pKey", "userPKey")).withRel("roles-unassignuser")
                )
        );
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/EYAWAQ
     **/
    @GetMapping(API_ROLES)
    public ResponseEntity<List<RoleVO>> findAllRoles() {
        List<RoleVO> result = service.findAll();
        result.forEach(this::replaceUsers);
        return ResponseEntity.ok(result);
    }

    @GetMapping(API_ROLES + "/{pKey}")
    public ResponseEntity<RoleVO> findRoleByPKey(
            @PathVariable("pKey") String pKey
    ) {
        RoleVO result = service.findByPKey(pKey);
        return ResponseEntity.ok(result);
    }

    @GetMapping(API_ROLES + "/{pKey}/users")
    public ResponseEntity<List<UserVO>> findUsersOfRole(
            @PathVariable("pKey") String pKey
    ) {
        RoleVO result = service.findByPKey(pKey);
        return ResponseEntity.ok(new ArrayList<>(result.getUsers()));
    }

    @GetMapping(API_ROLES + "/{pKey}/grants")
    public ResponseEntity<List<SecurityObjectVO>> findGrantsOfRole(
            @PathVariable("pKey") String pKey
    ) {
        RoleVO result = service.findByPKey(pKey);
        return ResponseEntity.ok(new ArrayList<>(result.getGrants()));
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/BIAWAQ
     */
    @PostMapping(API_ROLES)
    @Validated(ValidationGroups.Create.class)
    public ResponseEntity<RoleVO> create(
            @RequestBody @Valid @NotNull RoleVO role,
            HttpServletRequest req
    ) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating Role [{}]", role);
        }
        RoleVO result = service.create(role);
        replaceUsers(result);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.LOCATION, super.getLocationForCreatedResource(req, result.getpKey()))
                .body(result);
    }

    @PutMapping(API_ROLES + "/{pKey}")
    @Validated(ValidationGroups.Modify.class)
    public ResponseEntity<RoleVO> save(
            @PathVariable("pKey") String pKey,
            @RequestBody @Valid @NotNull RoleVO role
    ) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating Role with pKey [{}] to [{}]", pKey, role);
        }
        RoleVO result = service.save(pKey, role);
        replaceUsers(result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(API_ROLES + "/{pKey}")
    public ResponseEntity<Void> delete(@PathVariable("pKey") String pKey) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Deleting Role with pKey [{}]", pKey);
        }
        service.delete(pKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(API_ROLES + "/{pKey}/users/{userPKey}")
    public ResponseEntity<RoleVO> assignUserToRole(
            @PathVariable("pKey") String pKey,
            @PathVariable("userPKey") String userPKey) {
        RoleVO role = service.assignUser(pKey, userPKey);
        replaceUsers(role);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping(API_ROLES + "/{pKey}/users/{userPKey}")
    public ResponseEntity<RoleVO> unassignUser(
            @PathVariable("pKey") String pKey,
            @PathVariable("userPKey") String userPKey) {
        RoleVO role = service.unassignUser(pKey, userPKey);
        replaceUsers(role);
        return ResponseEntity.ok(role);
    }

    private void replaceUsers(RoleVO role) {
        role.add(linkTo(methodOn(RoleController.class).findUsersOfRole(role.getpKey())).withRel("users"));
        role.getUsers().clear();
        role.add(linkTo(methodOn(RoleController.class).findGrantsOfRole(role.getpKey())).withRel("grants"));
        role.getGrants().clear();
    }
}
