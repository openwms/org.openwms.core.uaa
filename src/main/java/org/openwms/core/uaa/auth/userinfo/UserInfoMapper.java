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
package org.openwms.core.uaa.auth.userinfo;

import org.ameba.annotation.Measured;
import org.ameba.annotation.TxService;
import org.openwms.core.uaa.admin.RoleMapper;
import org.openwms.core.uaa.admin.SecurityObjectMapper;
import org.openwms.core.uaa.admin.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * A UserInfoMapper is used to extend the OpenID Connect 1.0 UserInfo response with additional information about the user.
 *
 * @author Heiko Scherrer
 * @see <a href="https://openid.net/specs/openid-connect-core-1_0.html#UserInfo">5.3. UserInfo Endpoint</a>
 */
@TxService
public class UserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    private final UserService userService;
    private final RoleMapper roleMapper;
    private final SecurityObjectMapper securityObjectMapper;

    public UserInfoMapper(UserService userService, RoleMapper roleMapper, SecurityObjectMapper securityObjectMapper) {
        this.userService = userService;
        this.roleMapper = roleMapper;
        this.securityObjectMapper = securityObjectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Measured
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext oidcUserInfoAuthenticationContext) {
        var principal = (JwtAuthenticationToken) oidcUserInfoAuthenticationContext.getAuthentication().getPrincipal();
        var claims = principal.getToken().getClaims();
        var c = new HashMap<String, Object>(claims.size());
        var user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(format("User with username [%s] not found", principal.getName())));
        c.putAll(claims);
        c.put("username", user.getUsername());
        user.setFullname(v -> c.put("name", v))
                .supplyPrimaryEmailAddress(v -> c.put("email", v.getEmailAddress()))
                .supplyLastPasswordChange(v -> c.put("last_password_change", v))
                .supplyRoles(v -> c.put("roles", roleMapper.convertToStrings(v)))
                .supplyGrants(v -> c.put("grants", securityObjectMapper.convertToStrings(v)))
        ;
        user.supplyUserDetails(ud -> {
            ud
                    .supplyIm(v -> c.put("im", v))
                    .supplyImage(v -> c.put("image", v))
                    .supplyDepartment(v -> c.put("department", v))
                    .supplyOffice(v -> c.put("office", v))
                    .supplyPhoneNo(v -> c.put("phone", v))
                    .supplyGender(v -> c.put("gender", v))
            ;
        });
        return new OidcUserInfo(c);
    }
}
