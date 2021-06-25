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
import org.openwms.core.uaa.api.ValidationGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
                        linkTo(methodOn(RoleController.class).create(new RoleVO(), null)).withRel("roles-create"),
                        linkTo(methodOn(RoleController.class).save("pKey", new RoleVO())).withRel("roles-save"),
                        linkTo(methodOn(RoleController.class).delete("pKey")).withRel("roles-delete")
                )
        );
    }

    /**
     * Documented here: https://openwms.atlassian.net/wiki/x/EYAWAQ
     **/
    @Transactional(readOnly = true)
    @GetMapping(API_ROLES)
    public ResponseEntity<List<RoleVO>> findAllRoles() {
        return ResponseEntity.ok(service.findAll());
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
        RoleVO vo = service.create(role);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.LOCATION, super.getLocationForCreatedResource(req, vo.getpKey()))
                .body(vo);
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
        return ResponseEntity.ok(service.save(pKey, role));
    }

    @DeleteMapping(API_ROLES + "/{pKey}")
    public ResponseEntity<Void> delete(@PathVariable("pKey") String pKey) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Deleting Role with pKey [{}]", pKey);
        }
        service.delete(pKey);
        return ResponseEntity.noContent().build();
    }
}
