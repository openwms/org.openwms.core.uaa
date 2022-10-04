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
package org.openwms.core.uaa.admin.impl;

import org.ameba.app.ValidationConfiguration;
import org.junit.jupiter.api.Test;
import org.openwms.core.uaa.api.EmailVO;
import org.openwms.core.uaa.api.UserDetailsVO;
import org.openwms.core.uaa.api.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A UserMapperImplIT.
 *
 * @author Heiko Scherrer
 */
@DataJpaTest
@Sql("classpath:test.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ValidationConfiguration.class)
class UserMapperImplIT {

    @Autowired
    private TestEntityManager em;

    @Test
    void testEOtoVO() {
        // arrange
        var emailMapper = new EmailMapperImpl();
        var testee = new UserMapperImpl(emailMapper);
        var eo = em.find(User.class, 1000L);
        em.flush();
        
        // act
        var vo = testee.convertToVO(eo);
        
        // assert
        assertVO(vo);
    }

    @Test
    void testEOtoEO() {
        // arrange
        var emailMapper = new EmailMapperImpl();
        var testee = new UserMapperImpl(emailMapper);
        var eo = em.find(User.class, 1000L);
        em.flush();

        // act
        var target = new User();
        testee.copy(eo, target);

        // assert
        assertCopiedEO(target);
    }

    @Test
    void testEOstoVOs() {
        // arrange
        var emailMapper = new EmailMapperImpl();
        var testee = new UserMapperImpl(emailMapper);
        var eo = em.find(User.class, 1000L);

        // act
        var vo = testee.convertToVO(List.of(eo));

        // assert
        assertVO(vo.get(0));
    }

    @Test
    void testVOtoEO() {
        // arrange
        var emailMapper = new EmailMapperImpl();
        var testee = new UserMapperImpl(emailMapper);
        var vo = UserVO.newBuilder()
                .pKey("pKey")
                .ol(1)
                .username("username")
                .emailAddresses(List.of(new EmailVO("admin.private@acme.com", true), new EmailVO("admin@acme.com", false)))
                .enabled(true)
                .expirationDate(ZonedDateTime.now())
                .extern(true)
                .fullname("fullname")
                .lastPasswordChange(ZonedDateTime.now())
                .locked(true)
                .roleNames(List.of("ADMIN"))
                .userDetails(new UserDetailsVO())
                .build();

        // act
        var eo = testee.convertFrom(vo);

        // assert
        assertEO(eo);
    }

    private static void assertVO(UserVO vo) {
        assertThat(vo.getpKey()).isNotBlank();
        assertThat(vo.getOl()).isOne();
        assertThat(vo.getUsername()).isNotBlank();
        assertThat(vo.getFullname()).isNotBlank();
        assertThat(vo.getEnabled()).isTrue();
        assertThat(vo.getLocked()).isTrue();
        assertThat(vo.getExtern()).isTrue();
        assertThat(vo.getRoleNames()).hasSize(1);
        assertThat(vo.getEmailAddresses()).hasSize(2);
        assertThat(vo.getLastPasswordChange()).isNotNull();
        assertThat(vo.getExpirationDate()).isNotNull();
    }

    private static void assertEO(User eo) {
        assertThat(eo.getPersistentKey()).isNotBlank();
        assertThat(eo.getOl()).isOne();
        assertThat(eo.getUsername()).isNotBlank();
        assertThat(eo.getFullname()).isNotBlank();
        assertThat(eo.isEnabled()).isTrue();
        assertThat(eo.isLocked()).isTrue();
        assertThat(eo.isExternalUser()).isTrue();
        assertThat(eo.getRoles()).isEmpty();
        assertThat(eo.getEmailAddresses()).hasSize(2);
        assertThat(eo.getLastPasswordChange()).isNotNull();
        assertThat(eo.getExpirationDate()).isNotNull();
        assertThat(eo.getCreatedBy()).isNull();
        assertThat(eo.getCreateDt()).isNull();
        assertThat(eo.getLastModifiedBy()).isNull();
        assertThat(eo.getLastModifiedDt()).isNull();
    }

    private static void assertCopiedEO(User eo) {
        assertThat(eo.getPersistentKey()).isNotBlank();
        assertThat(eo.getOl()).isOne();
        assertThat(eo.getUsername()).isNotBlank();
        assertThat(eo.getFullname()).isNotBlank();
        assertThat(eo.isEnabled()).isTrue();
        assertThat(eo.isLocked()).isTrue();
        assertThat(eo.isExternalUser()).isTrue();
        assertThat(eo.getRoles()).hasSize(1);
        assertThat(eo.getEmailAddresses()).hasSize(2);
        assertThat(eo.getLastPasswordChange()).isNotNull();
        assertThat(eo.getExpirationDate()).isNotNull();
        assertThat(eo.getCreatedBy()).isNull();
        assertThat(eo.getCreateDt()).isNull();
        assertThat(eo.getLastModifiedBy()).isNull();
        assertThat(eo.getLastModifiedDt()).isNull();
    }
}
