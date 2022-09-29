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
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openwms.core.uaa.api.ClientVO.Builder.aClientVO;

/**
 * A ClientVOTest.
 *
 * @author Heiko Scherrer
 */
class ClientVOTest {

    @Test
    void testEquality() {
        var original = aClientVO()
                .clientId("cliendId")
                .clientSecret("secr3t")
                .authorizedGrantTypes(asList("implicit"))
                .scopes(asList("client"))
                .webServerRedirectUris(asList("url"))
                .build();
        var same = aClientVO()
                .clientId("cliendId")
                .clientSecret("secr3t")
                .authorizedGrantTypes(asList("implicit"))
                .scopes(asList("client"))
                .webServerRedirectUris(asList("url"))
                .build();

        assertThat(original).isEqualTo(same);
        var clients = new HashSet<>();
        clients.add(original);
        clients.add(same);
        assertThat(clients).hasSize(1);
    }
}
