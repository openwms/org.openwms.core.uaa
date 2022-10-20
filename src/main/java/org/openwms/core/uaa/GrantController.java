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
import org.openwms.core.http.AbstractWebController;
import org.openwms.core.http.Index;
import org.openwms.core.uaa.api.GrantVO;
import org.openwms.core.uaa.api.ValidationGroups;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;
import static org.openwms.core.uaa.api.UAAConstants.API_GRANTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A GrantController.
 *
 * @author Heiko Scherrer
 */
@Validated
@MeasuredRestController
public class GrantController extends AbstractWebController {

    private final GrantService grantService;
    private final GrantMapper mapper;

    public GrantController(GrantService grantService, GrantMapper mapper) {
        this.grantService = grantService;
        this.mapper = mapper;
    }

    @GetMapping(API_GRANTS + "/index")
    public ResponseEntity<Index> index() {
        return ResponseEntity.ok(

                new Index(
                        linkTo(methodOn(GrantController.class).findByPKey("pKey")).withRel("grant-findbypkey"),
                        linkTo(methodOn(GrantController.class).findAllGrants()).withRel("grant-findall"),
                        linkTo(methodOn(GrantController.class).findAllForUser("user")).withRel("grant-findallforuser"),
                        linkTo(methodOn(GrantController.class).createGrant(new GrantVO(), null)).withRel("grant-create")
                )
        );
    }

    @GetMapping(value = API_GRANTS + "/{pKey}")
    public ResponseEntity<GrantVO> findByPKey(@PathVariable("pKey") String pKey) {

        var result = mapper.convertToVO(grantService.findByPKey(pKey));
        addSelfLink(result);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE)
                .body(result);
    }

    @Transactional(readOnly = true)
    @GetMapping(API_GRANTS)
    public ResponseEntity<List<GrantVO>> findAllGrants() {

        var grants = grantService.findAllGrants();
        var vos = mapper.convertToVOs(grants);
        vos.forEach(this::addSelfLink);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE)
                .body(vos);
    }

    @Transactional(readOnly = true)
    @GetMapping(path = API_GRANTS, headers = HEADER_VALUE_X_IDENTITY)
    public ResponseEntity<List<GrantVO>> findAllForUser(@NotBlank @RequestHeader(HEADER_VALUE_X_IDENTITY) String user) {

        var grants = grantService.findAllFor(user);
        var vos = mapper.convertToVOs(grants);
        if (vos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        vos.forEach(this::addSelfLink);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE)
                .body(vos);
    }

    @Validated(ValidationGroups.Create.class)
    @PostMapping(API_GRANTS)
    public ResponseEntity<GrantVO> createGrant(@Valid @RequestBody @NotNull GrantVO grant, HttpServletRequest req) {

        var eo = mapper.convertToEO(grant);
        var created = grantService.create(eo);
        var result = mapper.convertToVO(created);
        addSelfLink(result);
        return ResponseEntity
                .created(super.getLocationURIForCreatedResource(req, result.getpKey()))
                .header(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE)
                .body(result);
    }

    private void addSelfLink(GrantVO result) {
        result.add(linkTo(methodOn(GrantController.class).findByPKey(result.getpKey())).withRel("grant-findbypkey"));
    }
}
