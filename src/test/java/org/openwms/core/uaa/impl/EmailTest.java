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

import org.junit.jupiter.api.Test;
import org.openwms.core.uaa.impl.Email;
import org.openwms.core.uaa.impl.User;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A EmailTest.
 *
 * @author Heiko Scherrer
 */
class EmailTest {

    @Test
    void testEquality() {
        var u1 = new User("1");
        var u2 = new User("2");
        var email1 = new Email(u1, "u1@acme.com");
        var email2 = new Email(u2, "u1@acme.com");
        var email3 = new Email(u1, "U1@acme.com");
        var email4 = new Email(u1, "u1@acme.com");

        assertThat(email1).isEqualTo(email3).isEqualTo(email4).isNotEqualTo(email2);
    }
}
