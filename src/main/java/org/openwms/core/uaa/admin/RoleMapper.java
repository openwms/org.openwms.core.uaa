/*
 * Copyright 2005-2022 the original author or authors.
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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.api.RoleVO;

import java.util.List;

/**
 * A RoleMapper.
 *
 * @author Heiko Scherrer
 */
@Mapper(implementationPackage = "org.openwms.core.uaa.admin.impl", uses = UserDetailsMapper.class)
public interface RoleMapper {

    List<String> convertToStrings(List<Role> eo);
    default String convertToString(Role eo) {
        return eo.getName();
    }

    @Mapping(source = "persistentKey", target = "pKey")
    RoleVO convertToVO(Role eo);

    List<RoleVO> convertToVO(List<Role> eo);

    @Mapping(source = "pKey", target = "persistentKey")
    @Mapping(source = "users", target = "users", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "grants", target = "grants", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Role convertFrom(RoleVO vo);
}
