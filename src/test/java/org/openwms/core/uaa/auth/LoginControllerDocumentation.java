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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.UAAApplicationTest;
import org.openwms.core.uaa.admin.UserService;
import org.openwms.core.uaa.admin.impl.Email;
import org.openwms.core.uaa.admin.impl.Role;
import org.openwms.core.uaa.admin.impl.User;
import org.openwms.core.uaa.admin.impl.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * A LoginControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@UAAApplicationTest
class LoginControllerDocumentation {

    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private OAuthHelper helper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        CommandLineRunner dataImporter(UserService service, PasswordEncoder encoder) {
            return args -> {
                Optional<User> tester = service.findByUsername("tester");
                if (tester.isEmpty()) {
                    User user = new User("tester");
                    user.setEnabled(true);
                    user.setExpirationDate(ZonedDateTime.now().plusDays(1));
                    user.setExternalUser(false);
                    user.setFullname("Mister Jenkins");
                    user.setLocked(false);
                    user.setRoles(asList(new Role("ROLE_USER")));
                    user.setEmailAddresses(new HashSet<>(asList(new Email(user, "tester.tester@example.com", true))));
                    UserDetails ud = new UserDetails();
                    ud.setComment("testing only");
                    ud.setDepartment("Dep. 1");
                    ud.setDescription("Just a test user");
                    ud.setGender(UserDetails.Gender.FEMALE);
                    ud.setIm("Skype:testee");
                    ud.setOffice("Off. 815");
                    ud.setPhoneNo("001-1234-56789");
                    user.setUserDetails(ud);
                    user.changePassword(encoder.encode("tester"), "tester", encoder);
                    service.create(user);
                }
            };
        }
    }

    /**
     * Do something before each test method.
     */
    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void shall_find_user() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("gateway");
        mockMvc.perform(
                get("/oauth/userinfo").with(bearerToken))
                .andDo(document("oauth-findUserInfo",
                        preprocessResponse(prettyPrint())/*,
                        responseFields(
                                fieldWithPath("pKey").description("The persistent key"),
                                fieldWithPath("username").description("The unique name of the User in the system"),
                                fieldWithPath("externalUser").description("If the User is authenticated by an external system"),
                                fieldWithPath("locked").description("If the User is locked and has no permission to login"),
                                fieldWithPath("enabled").description("If the User is enabled in general")
                        )*/
                ))
        //        .andExpect(status().isOk())
        ;
    }
}
