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
import org.openwms.core.uaa.api.GrantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.openwms.core.uaa.api.UAAConstants.API_GRANTS;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * A GrantControllerDocumentation.
 *
 * @author Heiko Scherrer
 */
@UAAApplicationTest
class GrantControllerDocumentation {

    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test void shall_build_index() throws Exception {
        mockMvc
                .perform(get(API_GRANTS + "/index"))
                .andDo(document("grants-index"))
                .andExpect(status().isOk())
        ;
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_create_role() throws Exception {
        var vo = GrantVO.Builder.aGrantVO()
                .name("VIEW_USERS")
                .description("Permission to view all users in UI")
                .build();

        var mvcResult = mockMvc.perform(
                        RestDocumentationRequestBuilders.post(API_GRANTS)
                                .content(objectMapper.writeValueAsString(vo))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("grant-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("@class").ignored(),
                                fieldWithPath("name").description("Unique name of the Grant"),
                                fieldWithPath("description").description("(Optional) A descriptive text for the Grant")
                        )
                ))
                .andExpect(status().isCreated())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.LOCATION)).isNotBlank();

        vo = GrantVO.Builder.aGrantVO()
                .name("SEC_UAA_USER_LOOKUP")
                .description("")
                .build();
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(API_GRANTS)
                                .content(objectMapper.writeValueAsString(vo))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("grant-create-exists",
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_find_all_Grants() throws Exception {
        mockMvc
                .perform(get(API_GRANTS))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(4)))
                .andDo(document("grant-findall"))
        ;
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_find_Grant_by_pKey() throws Exception {
        mockMvc
                .perform(get(API_GRANTS + "/3"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andDo(document("grant-findbypkey",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("@class").description("Type identifier of the Grant type"),
                                fieldWithPath("links[]").description("Contains an array of hypermedia resource links"),
                                fieldWithPath("links[].*").ignored(),
                                fieldWithPath("ol").description("Versioning field to check the instance version"),
                                fieldWithPath("pKey").description("The technical persistent identifier of the Grant"),
                                fieldWithPath("name").description("Unique name of the Grant"),
                                fieldWithPath("description").description("A descriptive text for the Grant")
                        )
                ))
        ;
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_find_all_Grants_for_User() throws Exception {
        mockMvc
                .perform(get(API_GRANTS).header(HEADER_VALUE_X_IDENTITY, "jenkins"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, GrantVO.MEDIA_TYPE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(4)))
                .andDo(document("grant-findallforuser"))
        ;
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_find_all_Grants_for_User_empty() throws Exception {
        mockMvc
                .perform(get(API_GRANTS).header(HEADER_VALUE_X_IDENTITY, "tester"))
                .andExpect(status().isNoContent())
                .andDo(document("grant-findallforuser-empty"))
        ;
    }

    @Sql(scripts = "classpath:test.sql")
    @Rollback
    @Test void shall_find_all_Grants_for_User_not_exist() throws Exception {
        mockMvc
                .perform(get(API_GRANTS).header(HEADER_VALUE_X_IDENTITY, "UNKNOWN"))
                .andExpect(status().isNotFound())
                .andDo(document("grant-findallforuser-404"))
        ;
    }
}
