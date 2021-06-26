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
import org.openwms.core.uaa.api.PasswordString;
import org.openwms.core.uaa.api.UserVO;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.openwms.core.uaa.api.UAAConstants.API_USERS;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A UserControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@Transactional
@Rollback
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

    @Test void shall_find_no_users() throws Exception {
        mockMvc.perform(get(API_USERS))
                .andDo(document("user-findNone"))
                .andExpect(status().isOk())
        ;
    }

    @Sql("classpath:test.sql")
    @Test void shall_change_password() throws Exception {
        PasswordString p = new PasswordString("pw");
        mockMvc.perform(post(API_USERS + "/96baa849-dd19-4b19-8c5e-895d3b7f405d/password")
                .content(objectMapper.writeValueAsString(p))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-changepassword"))
                //.andExpect(status().isOk())
        ;
    }

    @Test void shall_create_user() throws Exception {
        UserVO vo = UserVO.newBuilder()
                .username("admin2")
                .email("admin@example.com")
                .build();

        // CREATE a new User
        MvcResult result = mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_USERS)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("links[]").ignored(),
                                fieldWithPath("username").description("The unique name of the User in the system"),
                                fieldWithPath("email").description("The email address used by the User, unique in the system")
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, notNullValue()))
                .andReturn();

        mockMvc.perform(
                RestDocumentationRequestBuilders.post(API_USERS)
                        .content(objectMapper.writeValueAsString(vo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-create-exists",
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isConflict())
                .andReturn();

        // FIND that newly created User
        String location = result.getResponse().getHeader(LOCATION);
        String contentAsString = mockMvc.perform(
                get(location))
                .andDo(document("user-findByPkey",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("pKey").description("The persistent key"),
                                fieldWithPath("username").description("The unique name of the User in the system"),
                                fieldWithPath("externalUser").description("If the User is authenticated by an external system"),
                                fieldWithPath("locked").description("If the User is locked and has no permission to login"),
                                fieldWithPath("enabled").description("If the User is enabled in general")
                        )
                ))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserVO userVO = objectMapper.readValue(contentAsString, UserVO.class);

        // MODIFY and SAVE the User
        userVO.setUsername("superuser");
        userVO.setLocked(true);
        userVO.setEnabled(false);
        contentAsString = mockMvc.perform(
                put(API_USERS)
                        .content(objectMapper.writeValueAsString(userVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-save", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("superuser")))
                .andExpect(jsonPath("$.locked", is(true)))
                .andExpect(jsonPath("$.enabled", is(false)))
                .andReturn().getResponse().getContentAsString()
        ;
        userVO = objectMapper.readValue(contentAsString, UserVO.class);
        assertThat(userVO.getUsername()).isEqualTo("superuser");

        String pKey = location.substring(location.substring(0, location.length()-1).lastIndexOf("/"));

        // Add an image to the User
        String s = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("pic.png").toURI())));
        mockMvc.perform(
                patch(API_USERS + "/" + pKey + "/details/image")
                        .content(s)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andDo(document("user-saveimage", preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
        ;

        contentAsString = mockMvc.perform(get(location)).andReturn().getResponse().getContentAsString();
        UserVO u = objectMapper.readValue(contentAsString, UserVO.class);
//        assertThat(u.getUserDetails().getImage()).isEqualTo(s);
        // DELETE the User
        mockMvc.perform(
                delete(API_USERS + pKey))
                .andDo(document("user-delete",  preprocessResponse(prettyPrint())))
                .andExpect(status().isNoContent());
    }
}
