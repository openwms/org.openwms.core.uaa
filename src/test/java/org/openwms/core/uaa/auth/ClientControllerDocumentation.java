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
import org.openwms.core.uaa.api.ClientVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.openwms.core.uaa.api.ClientVO.Builder.aClientVO;
import static org.openwms.core.uaa.api.UAAConstants.API_CLIENTS;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A GrantControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@Transactional
@Rollback
@UAAApplicationTest
class ClientControllerDocumentation {

    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Do something before each test method.
     */
    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test void shall_build_index() throws Exception {
        mockMvc.perform(
                get(API_CLIENTS + "/index")
        )
                .andDo(document("clients-index"))
                .andExpect(status().isOk());
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_demo_clients() throws Exception {
        mockMvc.perform(get(API_CLIENTS))
                .andDo(document("client-findAll"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void shall_create_client() throws Exception {
        var vo = aClientVO()
                .clientId("cliendId")
                .clientSecret("secr3t")
                .authorizedGrantTypes(asList("implicit"))
                .scopes(asList("client"))
                .webServerRedirectUris(asList("url"))
                .build();

        mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_CLIENTS)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("client-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("clientId").description("The unique id of the client"),
                                fieldWithPath("clientSecret").description("The clients secret used for authentication"),
                                fieldWithPath("scopes").description("A list of scopes the client requests authorization for"),
                                fieldWithPath("webServerRedirectUris").description("The OAuth2 redirect url"),
                                fieldWithPath("authorizedGrantTypes").description("The OAuth2 grant types the client is allowed to use")
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Sql("classpath:test.sql")
    @Test
    void shall_save_client() throws Exception {
        var vo = aClientVO()
                .pKey("1000")
                .clientId("cliendId")
                .clientSecret("secr3t")
                .authorizedGrantTypes(asList("implicit"))
                .scopes(asList("client"))
                .webServerRedirectUris(asList("url"))
                .build();

        MvcResult mvcResult = mockMvc.perform(
                RestDocumentationRequestBuilders.put(API_CLIENTS)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("client-save",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("links[]").ignored(),
                                fieldWithPath("pKey").description("The persistent key must be passed when modifying an existing instance"),
                                fieldWithPath("clientId").ignored(),
                                fieldWithPath("clientSecret").ignored(),
                                fieldWithPath("accessTokenValidity").ignored(),
                                fieldWithPath("additionalInformation").ignored(),
                                fieldWithPath("authorities").ignored(),
                                fieldWithPath("authorizedGrantTypes").ignored(),
                                fieldWithPath("autoapprove").ignored(),
                                fieldWithPath("refreshTokenValidity").ignored(),
                                fieldWithPath("resourceIds").ignored(),
                                fieldWithPath("scope").ignored(),
                                fieldWithPath("webServerRedirectUri").ignored()
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
        ClientVO resp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientVO.class);
        assertThat(resp.getClientId()).isEqualTo("cliendId");
        assertThat(resp.getClientSecret()).isEqualTo("secr3t");
        assertThat(resp.getAuthorizedGrantTypes()).contains("implicit");
        assertThat(resp.getScopes()).contains("client");
        assertThat(resp.getWebServerRedirectUris()).contains("url");
    }

    @Sql("classpath:test.sql")
    @Test
    void shall_delete_client() throws Exception {
        mockMvc.perform(
                delete(API_CLIENTS + "/1000"))
                .andDo(document("client-delete",  preprocessResponse(prettyPrint())))
                .andExpect(status().isNoContent());
    }
}
