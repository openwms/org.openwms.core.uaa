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
package org.openwms.core.uaa.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.UAAApplicationTest;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.UAAConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.openwms.core.uaa.api.UAAConstants.API_ROLES;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A RoleControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@UAAApplicationTest
class RoleControllerDocumentation {

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
                get(UAAConstants.API_ROLES + "/index")
        )
                .andDo(document("roles-index"))
                .andExpect(status().isOk());
    }

    @Sql("classpath:test.sql")
    @Test
    void shall_create_role() throws Exception {
        RoleVO vo = RoleVO.newBuilder()
                .name("ROLE_DEV")
                .description("Developers")
                .immutable(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_ROLES)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("role-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("links[]").ignored(),
                                fieldWithPath("name").description("Unique name of the Role"),
                                fieldWithPath("immutable").description("Whether or not this Role is immutable. Immutable Roles can't be modified"),
                                fieldWithPath("description").description("A descriptive text for the Role")
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.LOCATION)).isNotBlank();

        vo = RoleVO.newBuilder()
                .name("ROLE_ADMIN")
                .description("")
                .immutable(true)
                .build();
        mvcResult = mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_ROLES)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("role-create-exists",
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_demo_roles() throws Exception {
        mockMvc.perform(get(API_ROLES))
                .andDo(document("role-findAll"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(status().isOk())
        ;
    }

    @Sql("classpath:test.sql")
    @Test
    void shall_save_role() throws Exception {
        RoleVO vo = RoleVO.newBuilder()
                .pKey("1")
                .name("ROLE_SUPER")
                .description("Administrators role")
                .immutable(false)
                .build();

        mockMvc.perform(
                RestDocumentationRequestBuilders.put(API_ROLES + "/" + 1)
                        .content(objectMapper.writeValueAsString(new RoleVO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("role-save-noname"))
                .andExpect(status().isBadRequest())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(
                RestDocumentationRequestBuilders.put(API_ROLES + "/" + 1)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("role-save",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("links[]").ignored(),
                                fieldWithPath("pKey").description("The persistent key must be passed when modifying an existing instance"),
                                fieldWithPath("name").description("The name as an identifying attribute of the existing Role, cannot be changed"),
                                fieldWithPath("description").description("A description text to update"),
                                fieldWithPath("immutable").description("Whether the Role is immutable or not")
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
        RoleVO resp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoleVO.class);
        assertThat(resp.getName()).isEqualTo("ROLE_SUPER");
        assertThat(resp.getDescription()).isEqualTo("Administrators role");
        assertThat(resp.getImmutable()).isTrue();
    }

    @Sql("classpath:test.sql")
    @Test
    void shall_delete_client() throws Exception {
        mockMvc.perform(
                delete(API_ROLES + "/1"))
                .andDo(document("role-delete",  preprocessResponse(prettyPrint())))
                .andExpect(status().isNoContent());
    }

    @Sql("classpath:test.sql")
    @Transactional
    @Test
    void shall_assign_user() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post(API_ROLES + "/1/users/96baa849-dd19-4b19-8c5e-895d3b7f405e"))
                .andDo(document("role-assign-user", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andReturn();
        RoleVO resp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoleVO.class);
        //assertThat(resp.getLinks().stream().filter(l -> l.getRel().value().equals("user"))).hasSize(2);
    }

    @Sql("classpath:test.sql")
    @Test
    void test_assign_unknown_user() throws Exception {
        mockMvc.perform(
                post(API_ROLES + "/1/users/UNKNOWN"))
                .andDo(document("role-assign-unknown-user", preprocessResponse(prettyPrint())))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Sql("classpath:test.sql")
    @Test
    void test_assign_unknown_role() throws Exception {
        mockMvc.perform(
                post(API_ROLES + "/UNKOWN/users/96baa849-dd19-4b19-8c5e-895d3b7f405d"))
                .andDo(document("role-assign-unknown-role", preprocessResponse(prettyPrint())))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Sql("classpath:test.sql")
    @Transactional
    @Test
    void shall_unassign_user() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                delete(API_ROLES + "/1/users/96baa849-dd19-4b19-8c5e-895d3b7f405d"))
                .andDo(document("role-unassign-user", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andReturn();
        RoleVO resp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoleVO.class);
        assertThat(resp.getLinks().stream().filter(l -> l.getRel().value().equals("user"))).isEmpty();
    }

    @Sql("classpath:test.sql")
    @Test
    void test_unassign_unknown_user() throws Exception {
        mockMvc.perform(
                delete(API_ROLES + "/1/users/UNKNOWN"))
                .andDo(document("role-unassign-unknown-user", preprocessResponse(prettyPrint())))
                .andExpect(status().isNotFound());
    }

    @Sql("classpath:test.sql")
    @Test
    void test_unassign_unknown_role() throws Exception {
        mockMvc.perform(
                delete(API_ROLES + "/UNKNOWN/users/96baa849-dd19-4b19-8c5e-895d3b7f405d"))
                .andDo(document("role-unassign-unknown-role", preprocessResponse(prettyPrint())))
                .andExpect(status().isNotFound());
    }
}
