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
import org.mapstruct.SubclassMapping;
import org.openwms.core.uaa.admin.impl.Grant;
import org.openwms.core.uaa.admin.impl.SecurityObject;
import org.openwms.core.uaa.admin.impl.UserDetails;
import org.openwms.core.uaa.api.GrantVO;
import org.openwms.core.uaa.api.SecurityObjectVO;
import org.openwms.core.uaa.api.UserDetailsVO;

import javax.validation.Valid;
import java.util.Base64;
import java.util.List;

/**
 * A SecurityObjectMapper.
 *
 * @author Heiko Scherrer
 */
@Mapper(implementationPackage = "org.openwms.core.uaa.admin.impl")
public interface SecurityObjectMapper {

    List<String> convertToStrings(List<SecurityObject> eo);
    default String convertToString(SecurityObject eo) {
        return eo.getName();
    }

    @Mapping(source = "pKey", target = "persistentKey")
    Grant convertToEO(GrantVO vo);

    @Mapping(source = "persistentKey", target = "pKey")
    GrantVO convertToVO(Grant eo);

    List<GrantVO> convertToGrantVOs(List<Grant> eo);

    @SubclassMapping(source = GrantVO.class, target = Grant.class)
    @Mapping(source = "pKey", target = "persistentKey")
    @Mapping(source = "name", target = "name")
    SecurityObject convertToEO(SecurityObjectVO vo);

    @Mapping(source = "persistentKey", target = "pKey")
    SecurityObjectVO convertToVO(SecurityObject eo);

    List<SecurityObjectVO> convertToVO(List<SecurityObject> eo);

    @Valid UserDetailsVO map(UserDetails eo);

    default byte[] map(String source) {
        if (source == null) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(source);
    }
}
