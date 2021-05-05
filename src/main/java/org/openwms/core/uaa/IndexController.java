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

import org.openwms.core.http.Index;
import org.openwms.core.uaa.admin.GrantController;
import org.openwms.core.uaa.admin.RoleController;
import org.openwms.core.uaa.admin.UserController;
import org.openwms.core.uaa.auth.ClientController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A IndexController serves the root {@link Index} resource via REST.
 *
 * @author Heiko Scherrer
 */
@RestController("uaaIndexController")
class IndexController {

    @GetMapping("/index")
    public ResponseEntity<Index> getIndex() {
        return ResponseEntity.ok(
                new Index(
                        linkTo(methodOn(UserController.class).index()).withRel("user-index"),
                        linkTo(methodOn(RoleController.class).index()).withRel("role-index"),
                        linkTo(methodOn(GrantController.class).index()).withRel("grant-index"),
                        linkTo(methodOn(ClientController.class).index()).withRel("client-index")
                )
        );
    }
}
