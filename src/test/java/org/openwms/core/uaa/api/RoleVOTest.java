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
package org.openwms.core.uaa.api;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A RoleVOTest.
 *
 * @author Heiko Scherrer
 */
class RoleVOTest {

    @Test
    void testEquality() {
        var original = RoleVO.Builder.newBuilder()
                .name("name")
                .immutable(true)
                .description("description")
                .build();
        var same = RoleVO.Builder.newBuilder()
                .name("name")
                .immutable(true)
                .description("description")
                .build();

        assertThat(original).isEqualTo(same);
        var roles = new HashSet<>();
        roles.add(original);
        roles.add(same);
        assertThat(roles).hasSize(1);
    }
}
