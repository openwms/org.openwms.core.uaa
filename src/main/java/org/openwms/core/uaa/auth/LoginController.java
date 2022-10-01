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
package org.openwms.core.uaa.auth;

import org.ameba.http.MeasuredRestController;
import org.openwms.core.http.AbstractWebController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * A LoginController.
 *
 * @author Heiko Scherrer
 */
@MeasuredRestController
public class LoginController extends AbstractWebController {

    private final UserDetailsService userDetailsService;

    LoginController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/claims")
    public ResponseEntity<Void> loginGet() {
        return ResponseEntity.ok().build();
    }
/*
    //@Secured("ROLE_USER")
    //@PreAuthorize("isAnonymous()")
    @GetMapping(value = "/oauth/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String user(Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        if (UserWrapper.class.equals(userDetails.getClass())) {
            User user = ((UserWrapper) userDetails).getUser();
            final String baseUrl =
                    ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            UserInfo userInfo = new UserInfo(new Subject(user.getUsername()));
            userInfo.setName(user.getUsername());
            Optional<Email> first = user.getEmailAddresses().stream().filter(Email::isPrimary).findFirst();
            if (first.isPresent()) {
                userInfo.setEmailAddress(first.get().toString());
            }
            userInfo.setPhoneNumber(user.getUserDetails().getPhoneNo());
            userInfo.setFamilyName(user.getFullname());
            userInfo.setGivenName(user.getFullname());
            userInfo.setPicture(URI.create(baseUrl + "/uaa/user/" + user.getPersistentKey() + "/image/"));
            userInfo.setGender(new Gender(user.getUserDetails().getGender().name()));
            return userInfo.toJSONObject().toJSONString();
        } else {
            UserInfo userInfo = new UserInfo(new Subject(principal.getName()));
            return userInfo.toJSONObject().toJSONString();
        }
    }

 */

    @PostMapping("/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }
}