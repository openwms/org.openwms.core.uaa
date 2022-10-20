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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openwms.core.UAAApplicationTest;
import org.openwms.core.uaa.api.EmailVO;
import org.openwms.core.uaa.api.PasswordString;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.SecurityObjectVO;
import org.openwms.core.uaa.api.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.openwms.core.uaa.api.UAAConstants.API_USERS;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A UserControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@UAAApplicationTest
class UserControllerDocumentation {

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
                get(API_USERS + "/index")
        )
                .andDo(document("users-index"))
                .andExpect(status().isOk());
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_demo_users() throws Exception {
        mockMvc.perform(get(API_USERS))
                .andDo(document("user-findAll"))
                .andExpect(status().isOk())
        ;
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_by_pKey() throws Exception {
        mockMvc.perform(get(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405d"))
                .andDo(document("user-findByPkey"))
                .andExpect(status().isOk())
        ;
    }

    @Test void shall_find_no_users() throws Exception {
        mockMvc.perform(get(API_USERS))
                .andDo(document("user-findNone"))
                .andExpect(status().isOk())
        ;
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_roles_of_user() throws Exception {
        var mvcResult = mockMvc.perform(get(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405d/roles"))
                .andDo(document("user-findRolesOfUser", preprocessResponse(prettyPrint())))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(status().isOk())
                .andReturn()
                ;
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(RoleVO.MEDIA_TYPE);
    }

    @Sql("classpath:test.sql")
    @Test void shall_find_grants_of_user() throws Exception {
        var mvcResult = mockMvc.perform(get(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405d/grants"))
                .andDo(document("user-findGrantsOfRUser", preprocessResponse(prettyPrint())))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(status().isOk())
                .andReturn()
                ;
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(SecurityObjectVO.MEDIA_TYPE);
    }

    @Sql("classpath:test.sql")
    @Test void shall_change_password() throws Exception {
        var p = new PasswordString("pw");
        var contentAsString = mockMvc.perform(post(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405d/password")
                        .content(objectMapper.writeValueAsString(p))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-changepassword"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var user = objectMapper.readValue(contentAsString, UserVO.class);
        assertThat(user.getLastPasswordChange()).isCloseTo(ZonedDateTime.now(), within(10, ChronoUnit.SECONDS));
    }

    @Sql("classpath:test.sql")
    @Test void shall_create_user() throws Exception {
        var vo = UserVO.newBuilder()
                .username("admin2")
                .emailAddresses(asList(new EmailVO("admin2@example.com", true)))
                .build();

        // CREATE a new User
        var result = mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_USERS)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("links[]").ignored(),
                                fieldWithPath("ol").ignored(),
                                fieldWithPath("username").description("The unique name of the User in the system"),
                                fieldWithPath("emailAddresses[]").description("The User's email addresses"),
                                fieldWithPath("emailAddresses[].emailAddress").description("The actual email address"),
                                fieldWithPath("emailAddresses[].primary").description("Whether this email address is the primary one used in the system. Each User can only have one primary email address")
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, notNullValue()))
                .andReturn();


        // FIND that newly created User
        var location = result.getResponse().getHeader(LOCATION);
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Sql("classpath:test.sql")
    @Test void shall_create_user_exists() throws Exception {
        var vo = UserVO.newBuilder()
                .username("tester")
                .emailAddresses(List.of(new EmailVO("tester@example.com", true)))
                .build();

        // Try again and validate the User already exists now
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(API_USERS)
                                .content(objectMapper.writeValueAsString(vo))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-create-exists",
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Sql("classpath:test.sql")
    @Test void shall_update_user() throws Exception {
        final String pKey = "96baa849-dd19-4b19-8c5e-895d3b7f405e";
        var vo = UserVO.newBuilder()
                .pKey(pKey)
                .username("superuser")
                .emailAddresses(List.of(new EmailVO("admin@example.com", true)))
                .build();
        // MODIFY and SAVE the User
        vo.setLocked(true);
        vo.setEnabled(false);
        var contentAsString = mockMvc.perform(
                        put(API_USERS)
                                .content(objectMapper.writeValueAsString(vo))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-save", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("superuser")))
                .andExpect(jsonPath("$.locked", is(true)))
                .andExpect(jsonPath("$.enabled", is(false)))
                .andReturn().getResponse().getContentAsString()
        ;
        vo = objectMapper.readValue(contentAsString, UserVO.class);
        assertThat(vo.getUsername()).isEqualTo("superuser");

        // Add an image to the User
        var s = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("pic.png").toURI())));
        mockMvc.perform(
                        post(API_USERS + "/" + pKey + "/details/image")
                                .content(s)
                                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andDo(document("user-saveimage", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
        ;

        // Update User password
        var pw = new PasswordString("welcome");
        mockMvc.perform(
                        post(API_USERS + "/" + pKey + "/password")
                                .content(objectMapper.writeValueAsString(pw))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-change-password", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
        ;
    }

    @Sql("classpath:test.sql")
    @Test void shall_delete_user() throws Exception {

        // DELETE the User
        mockMvc.perform(
                        delete(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405e"))
                .andDo(document("user-delete",  preprocessResponse(prettyPrint())))
                .andExpect(status().isNoContent());
    }
}
