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
package org.openwms.core.uaa;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.openwms.core.uaa.impl.Role;
import org.openwms.core.uaa.impl.User;
import org.openwms.core.uaa.api.UserVO;

import java.util.Base64;
import java.util.List;

/**
 * A UserMapper.
 *
 * @author Heiko Scherrer
 */
@Mapper(implementationPackage = "org.openwms.core.uaa.impl", uses = {EmailMapper.class, UserDetailsMapper.class})
public interface UserMapper {

    List<String> convertToStrings(List<Role> eo);
    default String convertToString(Role eo) {
        return eo.getName();
    }

    @Mapping(source = "persistentKey", target = "pKey")
    @Mapping(source = "ol", target = "ol")
    @Mapping(source = "externalUser", target = "extern")
    @Mapping(source = "roles", target = "roleNames")
    UserVO convertToVO(User eo);
    List<UserVO> convertToVO(List<User> eo);

    @Mapping(source = "pKey", target = "persistentKey")
    @Mapping(source = "extern", target = "externalUser")
    @Mapping(target = "supplyLastPasswordChange", ignore = true)
    @Mapping(target = "supplyRoles", ignore = true)
    @Mapping(target = "supplyGrants", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "supplyPrimaryEmailAddress", ignore = true)
    @Mapping(target = "supplyUserDetails", ignore = true)
    @Mapping(target = "grants", ignore = true)
    @Mapping(target = "passwords", ignore = true)
    User convertFrom(UserVO vo);

    @AfterMapping
    default void enhanceEmail(@MappingTarget User parent){
        var childList = parent.getEmailAddresses();
        if (childList != null) {
            childList.forEach(child -> child.setUser(parent));
        }
    }

    @Mapping(source = "persistentKey", target = "persistentKey", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "supplyLastPasswordChange", ignore = true)
    @Mapping(target = "supplyRoles", ignore = true)
    @Mapping(target = "supplyGrants", ignore = true)
    @Mapping(target = "supplyPrimaryEmailAddress", ignore = true)
    @Mapping(target = "supplyUserDetails", ignore = true)
    void copy(User source, @MappingTarget User target);

    default byte[] map(String source) {
        if (source == null) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(source);
    }
}
