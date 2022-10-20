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
package org.openwms.core.uaa.impl;

import org.ameba.app.ValidationConfiguration;
import org.junit.jupiter.api.Test;
import org.openwms.core.uaa.api.RoleVO;
import org.openwms.core.uaa.api.UserVO;
import org.openwms.core.uaa.impl.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A RoleMapperImplIT.
 *
 * @author Heiko Scherrer
 */
@DataJpaTest
@Sql("classpath:test.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ValidationConfiguration.class)
class RoleMapperImplIT {

    @Autowired
    private TestEntityManager em;

    @Test
    void testEOtoVO() {
        // arrange
        var testee = new RoleMapperImpl(new UserMapperImpl(new EmailMapperImpl(), new UserDetailsMapperImpl()), new SecurityObjectMapperImpl());
        var eo = em.find(Role.class, 1000L);
        
        // act
        var vo = testee.convertToVO(eo);
        
        // assert
        assertRole(vo);
    }

    @Test
    void testEOstoVOs() {
        // arrange
        var testee = new RoleMapperImpl(new UserMapperImpl(new EmailMapperImpl(), new UserDetailsMapperImpl()), new SecurityObjectMapperImpl());
        var eo = em.find(Role.class, 1000L);

        // act
        var vo = testee.convertToVO(List.of(eo));

        // assert
        assertRole(vo.get(0));
    }

    @Test
    void testVOtoEO() {
        // arrange
        var testee = new RoleMapperImpl(new UserMapperImpl(new EmailMapperImpl(), new UserDetailsMapperImpl()), new SecurityObjectMapperImpl());
        var vo = RoleVO.newBuilder()
                .pKey("pKey")
                .ol(1)
                .name("name")
                .description("description")
                .immutable(true)
                .users(Set.of(UserVO.newBuilder().username("user").build()))
                .grants(Set.of(RoleVO.newBuilder().name("name").build()))
                .build();

        // act
        var eo = testee.convertFrom(vo);

        // assert
        assertRole(eo);
    }

    private static void assertRole(RoleVO vo) {
        assertThat(vo.getpKey()).isNotBlank();
        assertThat(vo.getOl()).isOne();
        assertThat(vo.getName()).isNotBlank();
        assertThat(vo.getDescription()).isNotBlank();
        assertThat(vo.getImmutable()).isTrue();
        assertThat(vo.getUsers()).hasSize(1);
        assertThat(vo.getGrants()).hasSize(4);
    }

    private static void assertRole(Role eo) {
        assertThat(eo.getPersistentKey()).isNotBlank();
        assertThat(eo.getOl()).isOne();
        assertThat(eo.getName()).isNotBlank();
        assertThat(eo.getDescription()).isNotBlank();
        assertThat(eo.getImmutable()).isTrue();
        assertThat(eo.getUsers()).hasSize(1);
        assertThat(eo.getGrants()).hasSize(1);
        assertThat(eo.getCreatedBy()).isNull();
        assertThat(eo.getCreateDt()).isNull();
        assertThat(eo.getLastModifiedBy()).isNull();
        assertThat(eo.getLastModifiedDt()).isNull();
    }
}
