/*
 * Copyright 2005-2021 the original author or authors.
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

/**
 * A MessageCodes.
 *
 * @author Heiko Scherrer
 */
public final class MessageCodes {

    private MessageCodes() { }

    /** Thrown if a Role has been looked up by persistent key but hasn't been found. */
    public static final String ROLE_WITH_PKEY_NOT_EXIST = "role.pkey.not.exist";
}
