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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A LoginControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@Sql({"classpath:test.sql"})
@UAAApplicationTest
class LoginControllerDocumentation {

    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private OAuthHelper helper;

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
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("sub").description("Subject is the logged in end-user"),
                                fieldWithPath("gender").description("The end-users gender"),
                                fieldWithPath("name").description("Name of the end-user"),
                                fieldWithPath("phone_number").description("Her phone number"),
                                fieldWithPath("given_name").description("The given name"),
                                fieldWithPath("family_name").description("Her family name"),
                                fieldWithPath("email").description("Her primary email address"),
                                fieldWithPath("picture").description("A link to a profile picture")
                        )
                ))
                .andExpect(status().isOk())
        ;
    }

   // @Test
    void shall_not_find_user() throws Exception {
        RequestPostProcessor bearerToken = helper.bearerToken("UNKNOWN");
        mockMvc.perform(
                get("/oauth/userinfo").with(bearerToken))
                .andDo(document("oauth-findUserInfo",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("sub").description("Subject is the logged in end-user"),
                                fieldWithPath("gender").description("The end-users gender"),
                                fieldWithPath("name").description("Name of the end-user"),
                                fieldWithPath("phone_number").description("Her phone number"),
                                fieldWithPath("given_name").description("The given name"),
                                fieldWithPath("family_name").description("Her family name"),
                                fieldWithPath("email").description("Her primary email address"),
                                fieldWithPath("picture").description("A link to a profile picture")
                        )
                ))
                .andExpect(status().isOk())
        ;
    }
}
